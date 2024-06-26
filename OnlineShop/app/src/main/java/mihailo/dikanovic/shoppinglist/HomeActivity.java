package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
{
    Button home;
    Button menu;
    Button account;
    Button bag;

    TextView welcome;
    TextView username;

    String user;

    AccountFragment account_fragment;
    MenuFragment menu_fragment;
    Intent intent;

    OnlineShopDB dbHelper;
    HttpHelper httpHelper;

    Button addItemButton;
    Button addCategoryButton;

    EditText itemName;
    EditText itemPrice;
    EditText itemCategory;
    EditText imageName;
    EditText addCategoryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new OnlineShopDB(this);
        home = findViewById(R.id.button_home);
        menu = findViewById(R.id.button_menu);
        account = findViewById(R.id.button_account);
        bag = findViewById(R.id.button_bag);
        welcome = findViewById(R.id.textView_welcome);
        username = findViewById(R.id.textView_username);

        intent = getIntent();
        user = intent.getStringExtra("username");
        username.setText(user);

        bag.setEnabled(false);
        home.setOnClickListener(this);
        account.setOnClickListener(this);
        menu.setOnClickListener(this);

        httpHelper = new HttpHelper();

        itemName = findViewById(R.id.itemNameEditText);
        itemPrice = findViewById(R.id.itemPriceEditText);
        itemCategory = findViewById(R.id.itemCategoryEditText);
        imageName = findViewById(R.id.imageNameEditText);
        addCategoryEditText = findViewById(R.id.addCategoryEditText);

        addItemButton = findViewById(R.id.addItemButton);
        addCategoryButton = findViewById(R.id.addCategoryButton);

        addItemButton.setOnClickListener(this);
        addCategoryButton.setOnClickListener(this);

        if(dbHelper.isAdmin(user))
        {
            addItemButton.setVisibility(View.VISIBLE);
            itemName.setVisibility(View.VISIBLE);
            itemPrice.setVisibility(View.VISIBLE);
            itemCategory.setVisibility(View.VISIBLE);
            imageName.setVisibility(View.VISIBLE);
            addCategoryEditText.setVisibility(View.VISIBLE);
            addCategoryButton.setVisibility(View.VISIBLE);
        }
        else
        {
            addItemButton.setVisibility(View.INVISIBLE);
            itemName.setVisibility(View.INVISIBLE);
            itemPrice.setVisibility(View.INVISIBLE);
            itemCategory.setVisibility(View.INVISIBLE);
            imageName.setVisibility(View.INVISIBLE);
            addCategoryEditText.setVisibility(View.INVISIBLE);
            addCategoryButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.button_account)
        {
            intent = getIntent();

            user = intent.getStringExtra("username");

            welcome.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
            menu.setBackgroundColor(getResources().getColor(R.color.black));
            addItemButton.setVisibility(View.INVISIBLE);
            itemName.setVisibility(View.INVISIBLE);
            itemPrice.setVisibility(View.INVISIBLE);
            itemCategory.setVisibility(View.INVISIBLE);
            imageName.setVisibility(View.INVISIBLE);
            addCategoryEditText.setVisibility(View.INVISIBLE);
            addCategoryButton.setVisibility(View.INVISIBLE);
            welcome.setVisibility(View.GONE);
            username.setVisibility(View.GONE);

            if (getSupportFragmentManager().findFragmentByTag("account_fragment_tag") == null)
            {
                getSupportFragmentManager().popBackStack();

                account_fragment = AccountFragment.newInstance(user, null);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_account_menu, account_fragment, "account_fragment_tag")
                        .addToBackStack(null)
                        .commit();
            }
        }
        else if(view.getId() == R.id.button_home)
        {
            getSupportFragmentManager().popBackStack();
            recreate();
        }
        else if(view.getId() == R.id.button_menu)
        {
            intent = getIntent();

            user = intent.getStringExtra("username");

            addItemButton.setVisibility(View.INVISIBLE);
            itemName.setVisibility(View.INVISIBLE);
            itemPrice.setVisibility(View.INVISIBLE);
            itemCategory.setVisibility(View.INVISIBLE);
            imageName.setVisibility(View.INVISIBLE);
            addCategoryEditText.setVisibility(View.INVISIBLE);
            addCategoryButton.setVisibility(View.INVISIBLE);
            welcome.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
            menu.setBackgroundColor(getResources().getColor(R.color.red_darker));

            if (getSupportFragmentManager().findFragmentByTag("menu_fragment_tag") == null)
            {
                getSupportFragmentManager().popBackStack();

                menu_fragment = MenuFragment.newInstance(user, null);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_account_menu, menu_fragment, "menu_fragment_tag")
                        .addToBackStack(null)
                        .commit();
            }
        }
        else if(view.getId() == R.id.addItemButton)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if(!httpHelper.postAddItemToCategory(itemName.getText().toString().trim(), itemPrice.getText().toString().trim(), itemCategory.getText().toString().trim(), imageName.getText().toString().trim()))
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(HomeActivity.this, "Couldn't add item.", Toast.LENGTH_LONG).show();
                                }
                            });
                            Thread.currentThread().stop();
                        }
                        else
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(HomeActivity.this, "Item added.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();

            dbHelper.insertItem(new ItemModel(itemCategory.getText().toString().trim(), getDrawableFromName(this, imageName.getText().toString().trim()), itemName.getText().toString().trim(), itemPrice.getText().toString().trim()));
            itemName.setText("");
            itemPrice.setText("");
            itemCategory.setText("");
            imageName.setText("");

        }
        else if(view.getId() == R.id.addCategoryButton)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if(!httpHelper.postAddCategory(addCategoryEditText.getText().toString()))
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(HomeActivity.this, "Couldn't add category.", Toast.LENGTH_LONG).show();
                                }
                            });
                            Thread.currentThread().stop();
                        }
                        else
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(HomeActivity.this, "Added new category.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
            addCategoryEditText.setText("");
        }
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
        getSupportFragmentManager().popBackStack();
        recreate();
    }

}