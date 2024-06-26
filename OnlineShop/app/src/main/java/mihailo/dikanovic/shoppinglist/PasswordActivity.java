package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity
{
    String user;

    EditText currentPass;
    EditText newPass;
    Button save;

    OnlineShopDB dbHelper;
    HttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        dbHelper = new OnlineShopDB(this);

        user = getIntent().getStringExtra("username");

        currentPass = findViewById(R.id.currentPass);
        newPass = findViewById(R.id.newPass);
        save = findViewById(R.id.button_save);

        httpHelper = new HttpHelper();

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            JSONObject jsonObject = httpHelper.putChangePassword(user, currentPass.getText().toString(), newPass.getText().toString());
                            if(jsonObject == null)
                            {
                                try
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        public void run() {
                                            Toast.makeText(PasswordActivity.this, "Failed to change password", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                Thread.currentThread().stop();
                            }

                            try
                            {
                                runOnUiThread(new Runnable()
                                {
                                    public void run() {
                                        Toast.makeText(PasswordActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            dbHelper.changePassword(user, jsonObject.getString("newPassword"));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}