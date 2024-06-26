package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    Button endSession;
    Button password;

    TextView username;
    TextView email;

    Intent intent;

    OnlineShopDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        endSession = findViewById(R.id.button_endSession);
        password = findViewById(R.id.button_password);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);

        dbHelper = new OnlineShopDB(this);

        intent = getIntent();

        username.setText(intent.getStringExtra("username"));
        email.setText(dbHelper.readEmailByUsername(username.getText().toString()));

        endSession.setOnClickListener(this);
        password.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_endSession)
        {
            intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else if (v.getId() == R.id.button_password)
        {
            String user = intent.getStringExtra("username");
            intent = new Intent(ProfileActivity.this, PasswordActivity.class);
            intent.putExtra("username", user);

            startActivity(intent);
        }
    }

}