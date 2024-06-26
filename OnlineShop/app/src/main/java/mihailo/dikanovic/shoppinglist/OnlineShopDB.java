package mihailo.dikanovic.shoppinglist;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OnlineShopDB extends SQLiteOpenHelper
{
    public static final String databaseName = "OnlineShopDB.db";
    public static final int databaseVersion = 1;

    //Tabela Korisnici
    public static final String tableUsersName = "Users";
    public static final String tableUsersColumnID = "ID";
    public static final String tableUsersColumnUsername = "Username";
    public static final String tableUsersColumnMail = "Mail";
    public static final String tableUsersColumnPassword = "Password";
    public static final String tableUsersColumnAdmin = "Admin";

    //Tabela Proizvodi
    public static final String tableItemsName = "Items";
    public static final String tableItemsColumnCategory = "Category";
    public static final String tableItemsColumnImage = "Image";
    public static final String tableItemsColumnName = "Name";
    public static final String tableItemsColumnPrice = "Price";


    //Tabela Istorija Kupovine
    public static final String tablePurchaseHistoryName = "PurchaseHistory";
    public static final String tablePurchaseHistoryColumnStatus = "Status";
    public static final String tablePurchaseHistoryColumnPrice = "Price";
    public static final String tablePurchaseHistoryColumnDate = "PurchaseDate";
    public static final String tablePurchaseHistoryColumnUser = "User";

    //Konstruktor
    public OnlineShopDB(Context context)
    {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE " + tableUsersName + " (" +
                        tableUsersColumnID + " TEXT, " +
                        tableUsersColumnUsername + " TEXT, " +
                        tableUsersColumnMail + " TEXT, " +
                        tableUsersColumnPassword + " TEXT, " +
                        tableUsersColumnAdmin + " INTEGER);"
        );

        db.execSQL(
                "CREATE TABLE " + tableItemsName + " (" +
                        tableItemsColumnCategory + " TEXT, " +
                        tableItemsColumnImage + " BLOB, " +
                        tableItemsColumnName + " TEXT, " +
                        tableItemsColumnPrice + " TEXT);"
        );

        db.execSQL(
                "CREATE TABLE " + tablePurchaseHistoryName + " (" +
                        tablePurchaseHistoryColumnStatus + " TEXT, " +
                        tablePurchaseHistoryColumnPrice + " TEXT, " +
                        tablePurchaseHistoryColumnDate + " TEXT, " +
                        tablePurchaseHistoryColumnUser + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }


    //Tabela Korisnici
    public void insertUser(User user)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(tableUsersColumnID, user.getID());
        values.put(tableUsersColumnUsername, user.getUsername());
        values.put(tableUsersColumnMail, user.getMail());
        values.put(tableUsersColumnPassword, user.getPassword());

        if(user.isAdmin())
        {
            values.put(tableUsersColumnAdmin, 1);
        }
        else
        {
            values.put(tableUsersColumnAdmin, 0);
        }

        db.insert(tableUsersName, null, values);
        db.close();
    }

    public int countUsers()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tableUsersName, new String[]{"COUNT(*)"}, null, null, null, null, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst())
        {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }

    public boolean checkUserCredentials(String username, String password)
    {
        SQLiteDatabase db = getReadableDatabase();

        String selection = tableUsersColumnUsername + " = ? AND " + tableUsersColumnPassword + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(tableUsersName, null, selection, selectionArgs, null, null, null);
        boolean valid = cursor.moveToFirst();

        cursor.close();
        db.close();
        return valid;
    }

    public boolean changePassword(String username, String newPassword)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(tableUsersColumnPassword, newPassword);

        String selection = tableUsersColumnUsername + " = ?";
        String[] selectionArgs = {username};

        int changedRows = db.update(tableUsersName, values, selection, selectionArgs);
        db.close();

        return changedRows > 0;
    }

    public String readEmailByUsername(String username)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tableUsersName, null, tableUsersColumnUsername + " = ?", new String[]{username}, null, null, null);

        String mail = null;
        if (cursor != null && cursor.moveToFirst())
        {
            mail = cursor.getString(cursor.getColumnIndexOrThrow(tableUsersColumnMail));
            cursor.close();
        }

        db.close();
        return mail;
    }

    public boolean isAdmin(String username)
    {
        SQLiteDatabase db = getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {tableUsersColumnAdmin};

        // Define the selection criteria
        String selection = tableUsersColumnUsername + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username};

        // Perform the query
        Cursor cursor = db.query(
                tableUsersName,    // The table to query
                projection,     // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,           // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        boolean isAdmin = false;
        if (cursor.moveToFirst())
        {
            int isAdminValue = cursor.getInt(cursor.getColumnIndexOrThrow(tableUsersColumnAdmin));
            isAdmin = isAdminValue == 1;
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return isAdmin;
    }


    //Tabela Proizvodi
    public void insertItem(ItemModel item)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(!itemExists(db, item.getName()))
        {
            ContentValues values = new ContentValues();

            Bitmap bitmap = drawableToBitmap(item.getImage());
            byte[] imageBytes = bitmapToByteArray(bitmap);

            values.put(tableItemsColumnCategory, item.getCategory());
            values.put(tableItemsColumnImage, imageBytes);
            values.put(tableItemsColumnName, item.getName());
            values.put(tableItemsColumnPrice, item.getPrice());

            db.insert(tableItemsName, null, values);
        }
        db.close();
    }

    public String[] readCategories()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tableItemsName, new String[]{tableItemsColumnCategory}, null, null, null, null, null);

        if(cursor == null || cursor.getCount() <= 0)
            return null;

        String[] categories = new String[cursor.getCount()];

        int i = 0;
        while(cursor.moveToNext())
        {
            categories[i++] = cursor.getString(cursor.getColumnIndexOrThrow(tableItemsColumnCategory));
        }

        cursor.close();
        db.close();

        return categories;
    }

    public ItemModel[] readItemsByCategory(Context context, String category)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tableItemsName, null, tableItemsColumnCategory + " = ?", new String[]{category}, null, null, null);

        if(cursor == null || cursor.getCount() <= 0)
            return null;

        ItemModel[] categoryItems = new ItemModel[cursor.getCount()];

        int i = 0;
        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(tableItemsColumnName));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(tableItemsColumnPrice));

            byte[] byteImage = cursor.getBlob(cursor.getColumnIndexOrThrow(tableItemsColumnImage));
            Drawable image = byteToDrawable(context, byteImage);

            categoryItems[i++] = new ItemModel(category, image, name, price);
        }

        cursor.close();
        db.close();

        return categoryItems;
    }


    //Tabela Istorija Kupovine
    public void insertPurchaseHistoryItem(ItemModel item, String username)
    {
        SQLiteDatabase db = getWritableDatabase();

        long timeMS = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date(timeMS));

        ContentValues values = new ContentValues();

        values.put(tablePurchaseHistoryColumnDate, date);
        values.put(tablePurchaseHistoryColumnPrice, item.getPrice());
        values.put(tablePurchaseHistoryColumnStatus, "WAITING FOR DELIVERY");
        values.put(tablePurchaseHistoryColumnUser, username);

        db.insert(tablePurchaseHistoryName, null, values);
        db.close();
    }

    public PurchaseModel[] readPurchaseHistory(String username)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(tablePurchaseHistoryName, null, tablePurchaseHistoryColumnUser + " = ?", new String[]{username}, null, null, null);

        if(cursor == null || cursor.getCount() <= 0)
            return null;

        PurchaseModel[] allEntries = new PurchaseModel[cursor.getCount()];

        int i = 0;
        while(cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(tablePurchaseHistoryColumnDate));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(tablePurchaseHistoryColumnPrice));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(tablePurchaseHistoryColumnStatus));

            PurchaseModel purchase = new PurchaseModel(status, price, date);
            allEntries[i++] = purchase;
        }

        cursor.close();
        db.close();

        return allEntries;
    }


    //Pomocne funkcije
    private boolean itemExists(SQLiteDatabase db, String itemName)
    {
        Cursor cursor = db.query(tableItemsName, new String[]{tableItemsColumnName},  tableItemsColumnName + " = ?", new String[]{itemName}, null, null, null);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        return exists;
    }

    public Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    private Drawable byteToDrawable(Context context, byte[] imageBytes)
    {
        if (imageBytes == null)
        {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public void clearDatabase()
    {
        SQLiteDatabase db = getWritableDatabase();

        //db.delete(tableUsersName, null, null);
        //db.delete(tableItemsName, null, null);
        db.delete(tablePurchaseHistoryName, null, null);

        db.close();
    }


}
