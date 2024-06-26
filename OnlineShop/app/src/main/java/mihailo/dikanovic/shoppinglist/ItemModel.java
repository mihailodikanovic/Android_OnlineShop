package mihailo.dikanovic.shoppinglist;

import android.graphics.drawable.Drawable;
import android.widget.Button;

public class ItemModel
{
    private String category;
    private Drawable image;
    private String name;
    private String price;

    public ItemModel(String category, Drawable image, String name, String price)
    {
        this.category = category;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
