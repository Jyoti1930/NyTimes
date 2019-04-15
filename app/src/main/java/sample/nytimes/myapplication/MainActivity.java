package sample.nytimes.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String URLstring = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/7.json?api-key=oGfZZPPb8L86dCbZ1jNMpJXRgbOd9efs";
    private static ProgressDialog mProgressDialog;
    private ListView listView;
    ArrayList<PopularArticleModel> dataModelArrayList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_articles);

        listView = (ListView) findViewById(R.id.articlelistview);

        if (haveNetworkConnection()) {
            retrieveJSON();
        } else {
            Toast.makeText(MainActivity.this,
                    "Internet is not connected. Please try after sometime...",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void retrieveJSON() {

        showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Json Response ", ">>" + response);

                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("status").equals("OK")) {

                                dataModelArrayList = new ArrayList<>();
                                JSONArray resultsArray = obj.getJSONArray("results");

                                for (int i = 0; i < resultsArray.length(); i++) {

                                    PopularArticleModel playerModel = new PopularArticleModel();
                                    JSONObject resultsObj = resultsArray.getJSONObject(i);

                                    playerModel.setTitle(resultsObj.getString("title"));
                                    playerModel.setByline(resultsObj.getString("byline"));
                                    playerModel.setPublisheddate(resultsObj.getString("published_date"));

                                    JSONArray mediaArray = resultsObj.getJSONArray("media");
                                    JSONObject mediaObj = mediaArray.getJSONObject(0);
                                    JSONArray mediaMetadataArray = mediaObj.getJSONArray("media-metadata");
                                    JSONObject mediaMetadataObj = mediaMetadataArray.getJSONObject(2);
                                    playerModel.setImgURL(mediaMetadataObj.getString("url"));


                                    dataModelArrayList.add(playerModel);
                                }
                                setupListview();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void setupListview() {
        removeSimpleProgressDialog();  //will remove progress dialog
        listAdapter = new ListAdapter(this, dataModelArrayList);
        listView.setAdapter(listAdapter);
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}


