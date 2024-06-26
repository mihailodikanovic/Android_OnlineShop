package mihailo.dikanovic.shoppinglist;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper
{
    private static final int SUCCESS = 200;
    private String BASE_URL = "http://147.91.162.160:3000";


    public JSONObject postRegisterUser(String username, String email, String password, boolean isAdmin) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/users";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        jsonObject.put("isAdmin", isAdmin?1:0);

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        InputStream inputStream = urlConnection.getErrorStream();
        if (inputStream == null)
        {
            inputStream = urlConnection.getInputStream();

        }
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = responseReader.readLine()) != null)
        {
            response.append(inputLine);
        }
        responseReader.close();

        String jsonString = response.toString();
        JSONObject jsonResponse = new JSONObject(jsonString);

        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSG" , urlConnection.getResponseMessage());

        urlConnection.disconnect();
        return responseCode == SUCCESS ? jsonResponse : null;
    }

    public boolean postLoginUser(String username, String password) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/login";
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSG" , urlConnection.getResponseMessage());

        urlConnection.disconnect();
        return (responseCode == SUCCESS);
    }

    public JSONObject putChangePassword(String username, String oldPassword, String newPassword) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/password";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("oldPassword", oldPassword);
        jsonObject.put("newPassword", newPassword);

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        InputStream inputStream = urlConnection.getErrorStream();
        if (inputStream == null)
        {
            inputStream = urlConnection.getInputStream();

        }
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = responseReader.readLine()) != null)
        {
            response.append(inputLine);
        }
        responseReader.close();

        String jsonString = response.toString();
        JSONObject jsonResponse = new JSONObject(jsonString);

        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSG" , urlConnection.getResponseMessage());

        urlConnection.disconnect();
        return responseCode == SUCCESS ? jsonResponse : null;
    }

    public boolean postAddCategory(String category) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/category";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", category);

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSG" , urlConnection.getResponseMessage());

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean postAddItemToCategory(String name, String price, String category, String imageName) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/item";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("price", price);
        jsonObject.put("category", category);
        jsonObject.put("imageName", imageName);

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return false;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSG" , urlConnection.getResponseMessage());

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getItemsByCategory(String category) throws IOException, JSONException
    {
        String urlString = BASE_URL + "/item/" + category;

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();

        Log.d("HTTP GET", "JSON data- " + jsonString);

        int responseCode =  urlConnection.getResponseCode();

        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    public JSONArray getCategories() throws IOException, JSONException
    {
        String urlString = BASE_URL + "/category";

        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();

        Log.d("HTTP GET", "JSON data- " + jsonString);

        int responseCode =  urlConnection.getResponseCode();

        urlConnection.disconnect();
        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }



    /*
        STARE FUNKCIJE
    */

    /*HTTP get json Array*/
    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        Log.d("HTTP GET", "JSON data- " + jsonString);
        int responseCode =  urlConnection.getResponseCode();
        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONArray(jsonString) : null;
    }

    /*HTTP get json object*/
    public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        Log.d("HTTP GET", "JSON obj- " + jsonString);
        int responseCode =  urlConnection.getResponseCode();
        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }

    /*HTTP post*/
    public JSONObject postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        /*header fields*/
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return null;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(responseCode));

        String responseMessage = urlConnection.getResponseMessage();
        Log.i("MSG", responseMessage);

        //Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line).append("\n");
        }
        br.close();
        String jsonString = sb.toString();

        urlConnection.disconnect();

        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }

    public JSONObject putJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        // Set the request method to PUT
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");

        // Enable output and input streams
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        // Write JSON object to the output stream
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode = urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(responseCode));

        String responseMessage = urlConnection.getResponseMessage();
        Log.i("MSG", responseMessage);

        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        String jsonString = sb.toString();

        urlConnection.disconnect();

        // Return response as JSONObject if successful
        return responseCode == SUCCESS ? new JSONObject(jsonString) : null;
    }


    /*HTTP delete*/
    public boolean httpDelete(String urlString) throws IOException, JSONException
    {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        /*header fields*/
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try
        {
            urlConnection.connect();
        }
        catch (IOException e)
        {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();
        Log.i("STATUS", String.valueOf(responseCode));

        String responseMessage = urlConnection.getResponseMessage();
        Log.i("MSG", responseMessage);

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }




}
