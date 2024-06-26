package mihailo.dikanovic.shoppinglist;

import java.util.ArrayList;

public class JNIexample
{
    static
    {
        System.loadLibrary("Library");
    }

    public native int sumPrices(ArrayList<Integer> prices);
}
