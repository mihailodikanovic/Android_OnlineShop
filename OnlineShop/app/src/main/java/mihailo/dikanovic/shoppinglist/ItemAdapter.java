package mihailo.dikanovic.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<ItemModel> items;

    OnlineShopDB dbHelper;
    String username;

    public ItemAdapter(Context context, String username)
    {
        this.context = context;
        this.username = username;
        items = new ArrayList<ItemModel>();
        dbHelper = new OnlineShopDB(context);
    }

    public void addItem(ItemModel item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void removeItems(ItemModel item)
    {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= 0)
        {
            return items.get(position);
        }
        else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, null);

            ViewHolder vh1 = new ViewHolder();
            vh1.img = convertView.findViewById(R.id.item_img);
            vh1.name = convertView.findViewById(R.id.item_name);
            vh1.price = convertView.findViewById(R.id.item_price);
            vh1.button = convertView.findViewById(R.id.item_button);

            convertView.setTag(vh1);
        }

        ItemModel item = (ItemModel) getItem(position);
        ViewHolder vh2 = (ViewHolder) convertView.getTag();
        vh2.img.setImageDrawable(item.getImage());
        vh2.name.setText(item.getName());
        vh2.price.setText(item.getPrice());

        vh2.button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbHelper.insertPurchaseHistoryItem(item, username);
                String msg = "Predmet " + item.getName() + " dodat u korpu.";
                Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private class ViewHolder
    {
        public ImageView img;
        public TextView name;
        public TextView price;
        public Button button;
    }

    public void update (ItemModel[] itemList)
    {
        items.clear();
        if (itemList != null)
        {
            for (ItemModel item : itemList)
            {
                items.add(item);
            }
        }
        notifyDataSetChanged();
    }

}
