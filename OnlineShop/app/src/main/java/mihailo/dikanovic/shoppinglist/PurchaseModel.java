package mihailo.dikanovic.shoppinglist;

public class PurchaseModel
{
    private String status;
    private String price;
    private String date;

    public PurchaseModel(String status, String price, String date)
    {
        this.status = status;
        this.price = price;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
