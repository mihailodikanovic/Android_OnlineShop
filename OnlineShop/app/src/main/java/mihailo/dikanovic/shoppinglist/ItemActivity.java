package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class ItemActivity extends AppCompatActivity {

    Intent intent;

    String category;
    String username;

    Button back;
    ListView list;
    TextView categoryName;
    TextView emptyList;

    ItemAdapter adapter;

    OnlineShopDB dbHelper;
    HttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        intent = getIntent();
        dbHelper = new OnlineShopDB(this);
        httpHelper = new HttpHelper();

        username = intent.getStringExtra("username");
        category = intent.getStringExtra("category");
        categoryName = findViewById(R.id.category_name);
        categoryName.setText(category);

        list = findViewById(R.id.list_items);
        emptyList = findViewById(R.id.list_text);

        adapter = new ItemAdapter(this, username);
        list.setAdapter(adapter);
        list.setEmptyView(emptyList);

        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new Thread(new Runnable() {
            public void run() {
                try
                {
                    JSONArray items = httpHelper.getItemsByCategory(category);

                    try
                    {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (items != null)
                                {
                                    ArrayList<ItemModel> itemList = new ArrayList<ItemModel>();

                                    for (int i = 0; i < items.length(); i++)
                                    {
                                        try
                                        {
                                            JSONObject item = items.getJSONObject(i);

                                            String name = item.getString("name");
                                            String price = item.getString("price");
                                            String category = item.getString("category");
                                            String imageName = item.getString("imageName");

                                            if (saleIsActive())
                                            {
                                                double discountedPrice = applyDiscount(price);
                                                price = String.valueOf((int)discountedPrice);
                                            }

                                            ItemModel newItem = new ItemModel(category, getDrawableFromName(ItemActivity.this, imageName), name, price);
                                            itemList.add(newItem);
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    for (ItemModel item : itemList)
                                    {
                                        adapter.addItem(item);
                                    }
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean saleIsActive()
    {
        SharedPreferences prefs = getSharedPreferences("SalePrefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("sale_active", false);
    }

    private double applyDiscount(String price)
    {
        return Double.parseDouble(price) * 0.8;
    }

    public Drawable getDrawableFromName(Context context, String imageName)
    {
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        if (resourceId != 0)
        {
            return context.getResources().getDrawable(resourceId, null);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }



}

