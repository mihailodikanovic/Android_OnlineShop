package mihailo.dikanovic.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity {

    ListView purchases;
    PurchaseAdapter adapter;
    OnlineShopDB dbHelper;
    Intent intent;
    String username;

    JNIexample jni;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        intent = getIntent();
        username = intent.getStringExtra("username");

        dbHelper = new OnlineShopDB(this);

        purchases = findViewById(R.id.list_purchases);
        purchases.setEmptyView(findViewById(R.id.empty_purchase_list));

        adapter = new PurchaseAdapter(this);
        purchases.setAdapter(adapter);

        jni = new JNIexample();
        int sum = 0;

        ArrayList<Integer> prices = new ArrayList<>();
        PurchaseModel[] purchases = dbHelper.readPurchaseHistory(username);

        for(PurchaseModel purchase: purchases)
        {
            prices.add(Integer.valueOf(purchase.getPrice()));
        }

        sum = jni.sumPrices(prices);
        Log.d("UKUPNA CENA", String.valueOf(sum));
        Log.d("UKUPNA CENA", String.valueOf(sum));
        Log.d("UKUPNA CENA", String.valueOf(sum));

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        PurchaseModel[] purchases = dbHelper.readPurchaseHistory(username);
        adapter.update(purchases);
    }


}