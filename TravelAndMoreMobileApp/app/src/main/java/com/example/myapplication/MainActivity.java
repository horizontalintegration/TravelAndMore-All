package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.util.Log;
import org.apache.http.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.*;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private String resp = "";
    private TextView title;
    private WebView mywebview;
    private ImageView imageBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.tv_title);
        mywebview = (WebView) findViewById(R.id.wv_result);
        imageBanner = (ImageView) findViewById(R.id.image_banner);
        String android_id = Settings.Secure.getString(MyApp.getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Button btnRun = (Button) findViewById(R.id.btn_run);
        btnRun.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {

               AsyncTaskRunner runner = new AsyncTaskRunner(MainActivity.this);
               String timeToSleep = "2";
               runner.execute(timeToSleep);
               try {
                   RequestParams params = new RequestParams();
                   JSONObject jsonParams = new JSONObject();
                   StringEntity entity = new StringEntity(jsonParams.toString());

                   RequestParams rp = new RequestParams();
                   //rp.add("username", "aaa"); rp.add("password", "aaa@123");

                   HttpUtils.get("api/Travel/Events", rp, new JsonHttpResponseHandler()
                   {
                       @Override
                       public void onSuccess(final int statusCode, final Header[] headers, final String responseBody) {
                           if (!responseBody.equals("")) {
                               try{
                                   JSONObject obj = new JSONObject(responseBody);

                                   String txtTitle = ((JSONObject)obj.get("eventDetail")).getString("title");
                                   title.setText(txtTitle);
                                   String bannerImage = ((JSONObject)obj.get("eventDetail")).getString("bannerImage");
                                   String txtSummary = ((JSONObject)obj.get("eventDetail")).getString("summary");
                                   mywebview.loadData(txtSummary, "text/html", "UTF-8");
                               }
                               catch (Exception e)
                               {
                                   e.printStackTrace();
                               }
                           }
                           else {
                               //finalResult.setText("Request succeeded!");
                           }
                       }

                       @Override
                       public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                           // If the response is JSONObject instead of expected JSONArray
                           Log.d("AsyncHttpClient", "---------------- this is response : " + response);
                           try {
                               resp = response.toString(2);

                           } catch (JSONException e) {
                               // TODO Auto-generated catch block
                               e.printStackTrace();
                           }
                           //finalResult.setText(resp);
                       }
                       @Override
                       public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                           // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                           Log.d("AsyncHttpClient", "---------------- this is response : " + errorResponse);
                           //finalResult.setText(errorResponse.toString());
                       }
                   });

               } catch (Exception e) {
                   e.printStackTrace();
                   resp = e.getMessage();
               }

               //finalResult.setText(resp);

               Log.d("Button Click", "Action triggered!");
           }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
