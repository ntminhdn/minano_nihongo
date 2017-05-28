package igp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.john.nguyen.minnanonihongovocabulary.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import igp.adapters.IgpAdapter;
import igp.objects.App;
import utils.Internet;
import utils.Util;

public class IgpActivity extends AppCompatActivity {

    private final boolean ENABLE_LOG = true;
    private final String TAG = "IGP";

    private TextView tvWarningNoInternet;
    private ListView mListView;
    private ArrayList<App> mList;

    private ProgressDialog mProgressDialog;

    private String mIgpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.igp_activity_more_app);

        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    public void refresh(View view){
        setWidgets();
    }

    //------------------------------------------
    private void init(){
        mIgpLink = getString(R.string.igp_link);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
        mProgressDialog.setMessage(getString(R.string.igp_loading));
    }

    private void getWidgets(){
        mListView = (ListView)findViewById(R.id.listview);
        tvWarningNoInternet = (TextView)findViewById(R.id.tvWarningNoInternet);
    }

    private void setWidgets(){
        if(Internet.isInternetAvailable(this)) {
            mListView.setVisibility(View.VISIBLE);
            tvWarningNoInternet.setVisibility(View.INVISIBLE);
            mProgressDialog.show();
            new FetchData().execute();
        }
        else{
            mListView.setVisibility(View.INVISIBLE);
            tvWarningNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void setWidgets(ArrayList<App> list){
        mList = list;
        IgpAdapter adapter = new IgpAdapter(this, R.layout.igp_item_app, mList);
        mListView.setAdapter(adapter);
    }

    private void addWidgetsListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Internet.isInternetAvailable(getApplicationContext())) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(i).getLink())));
                }
                else{
                    Util.showToast(getApplicationContext(), R.string.internet_no_internet);
                }
            }
        });
    }

    private class FetchData extends AsyncTask<Void, Void, ArrayList<App>> {

        private final String KEY_APP                = "apps";

        @Override
        protected ArrayList<App> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = "";
            ArrayList<App> moreApp = new ArrayList<>();

            try {
                URL url = new URL(mIgpLink);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                jsonStr = buffer.toString();
                log(jsonStr);

                JSONObject jsonResponse = new JSONObject(jsonStr);
                JSONArray jsonArrayHiragana = jsonResponse.getJSONArray(KEY_APP);
                for( int i = 0; i < jsonArrayHiragana.length(); i++){
                    JSONObject jsonObjectSub = jsonArrayHiragana.getJSONObject(i);
                    App app = parseApp(jsonObjectSub);
                    if(app.isShow()) {
                        moreApp.add(app);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        log("Error closing stream");
                    }
                }
            }
            return moreApp;
        }

        @Override
        protected void onPostExecute(ArrayList<App> moreApp) {
            super.onPostExecute(moreApp);
            setWidgets(moreApp);
            mProgressDialog.dismiss();
        }

        private App parseApp(JSONObject object){
            App app = new App();
            try {
                app.setName(object.getString(App.FIELD_NAME));
                app.setLink(object.getString(App.FIELD_LINK));
                app.setIcon(object.getString(App.FIELD_ICON));
                app.setDecription(object.getString(App.FIELD_DESCRIPTION));
                app.setShow(object.getBoolean(App.FIELD_SHOW));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return app;
        }
    }

    private void log(String str){
        if(ENABLE_LOG){
            Log.d(TAG, str);
        }
    }
}
