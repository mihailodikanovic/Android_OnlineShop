package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;
    Button register;

    LoginFragment login_fragment;
    RegisterFragment register_fragment;

    OnlineShopDB dbHelper;

    private static final String CHANNEL_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);

        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        dbHelper = new OnlineShopDB(this);

        createNotificationChannel();

        Intent serviceIntent = new Intent(this, SalePromotionService.class);
        startService(serviceIntent);

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.button_login)
        {
            login_fragment = LoginFragment.newInstance(null, null);

            if (!login_fragment.isAdded())
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_logreg, login_fragment)
                        .addToBackStack(null)
                        .commit();
                login.setVisibility(View.INVISIBLE);
                register.setVisibility(View.INVISIBLE);
            }
        }

        else if(view.getId() == R.id.button_register)
        {
            register_fragment = RegisterFragment.newInstance(null, null);

            if (!register_fragment.isAdded())
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_logreg, register_fragment)
                        .addToBackStack(null)
                        .commit();
                login.setVisibility(View.INVISIBLE);
                register.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void createNotificationChannel()
    {
        CharSequence name = "Sale Notification";
        String description = "Channel for sale notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        recreate();
    }


}