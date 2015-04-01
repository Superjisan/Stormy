package superjisan.com.stormy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "7d0ff8fcc4044e030dc77f34aabfd1a6";
        double latitude = 37.826;
        double longitude = -122.423 ;
        String forecasturl  = "https://api.forecast.io/forecast/"+apiKey+"/"+
                latitude+","+longitude;
         if(isNetworkAvailable()) {
             OkHttpClient client = new OkHttpClient();
             Request request = new Request.Builder()
                     .url(forecasturl)
                     .build();

             Call call = client.newCall(request);
             call.enqueue(new Callback() {
                 @Override
                 public void onFailure(Request request, IOException e) {
                     alertUserAboutError();
                 }

                 @Override
                 public void onResponse(Response response) throws IOException {
                     try {
                         Log.v(TAG, response.body().string());
                         if (response.isSuccessful()) {

                         } else {
                             alertUserAboutError();
                         }
                     } catch (IOException e) {
                         Log.e(TAG, "Exception caught", e);
                     }
                 }
             });

         }else{
             Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
         }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
