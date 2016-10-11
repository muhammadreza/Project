package alone.fb_login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by alone on 12/29/2015.
 */
public class addLocation extends Activity{

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        btn1=(Button) findViewById(R.id.simpan);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checknternetConnection();
                GetData dataambil = new GetData();
                dataambil.kata_tunggu = "Loading";
                dataambil.execute();
            }
        });
    }

    private String DownloadText(String s) {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        in = openHttpConnection(s);
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }

    private InputStream openHttpConnection(String s){
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(s);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)){
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            resCode=httpURLConnection.getResponseCode();
            if (resCode== HttpURLConnection.HTTP_OK){
                in=httpURLConnection.getInputStream();
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return in;
    }
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(addLocation.this);
        String kata_tunggu;
        String hasilData;
        String text=findViewById(R.id.editText).toString();
        @Override
        protected String doInBackground(String... params) {
            String urlnya = "http://192.168.137.1:81/tambalban/add_tb.php?nama="+text+"&lat=7.11&lon=11.657";
            hasilData = DownloadText(urlnya);
            return null;
        }
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(kata_tunggu);
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            this.dialog.dismiss();
        }
    }

    private boolean checknternetConnection(){
        ConnectivityManager connec = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if(connec.getNetworkInfo(0).getState()== NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(1).getState()==NetworkInfo.State.CONNECTING){
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
            return true;
        }
        else if(connec.getNetworkInfo(0).getState()== NetworkInfo.State.DISCONNECTED||
                connec.getNetworkInfo(1).getState()== NetworkInfo.State.DISCONNECTED){
            Toast.makeText(this,"Not Connected",Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
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
