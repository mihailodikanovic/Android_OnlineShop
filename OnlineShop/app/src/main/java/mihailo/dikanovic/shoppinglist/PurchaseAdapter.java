package mihailo.dikanovic.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PurchaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PurchaseModel> purchases;

    public PurchaseAdapter(Context context) {
        this.context = context;
        purchases = new ArrayList<PurchaseModel>();
    }

    public void addPurchase(PurchaseModel purchase)
    {
        purchases.add(purchase);
        notifyDataSetChanged();
    }

    public void removePurchase(PurchaseModel purchase)
    {
        purchases.remove(purchase);
        notifyDataSetChanged();
    }

    public void clearHistory()
    {
        purchases.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return purchases.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (position >= 0)
        {
            return purchases.get(position);
        }
        else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.purchase_row, null);

            PurchaseAdapter.ViewHolder vh1 = new PurchaseAdapter.ViewHolder();
            vh1.status = convertView.findViewById(R.id.status_box);
            vh1.price = convertView.findViewById(R.id.price_box);
            vh1.date = convertView.findViewById(R.id.date_box);

            convertView.setTag(vh1);
        }

        PurchaseModel purchase = (PurchaseModel) getItem(position);
        PurchaseAdapter.ViewHolder vh2 = (PurchaseAdapter.ViewHolder) convertView.getTag();
        vh2.status.setText(purchase.getStatus());
        vh2.price.setText(purchase.getPrice());
        vh2.date.setText(purchase.getDate());


        return convertView;
    }

    private class ViewHolder
    {
        public TextView status;
        public TextView price;
        public TextView date;
    }

    public void update (PurchaseModel[] purchaseList)
    {
        purchases.clear();
        if (purchaseList != null)
        {
            for (PurchaseModel purchase : purchaseList)
            {
                purchases.add(purchase);
            }
        }
        notifyDataSetChanged();
    }
}
