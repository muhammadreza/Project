package com.example.alone.json;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Bitmap bitmap=null;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=(Button) findViewById(R.id.tombol);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checknternetConnection();
                downloadImage("https://goo.gl/ksaiiu");
            }
        });
    }

    private void downloadImage(String s) {
        progressDialog = ProgressDialog.show(this, "", "Downloading Image from "+ s);
        final String url = s;
        new Thread(){
            public void run(){
                InputStream in = null;

                Message msg =Message.obtain();
                msg.what = 1;

                try {
                    in = openHttpConnection(url);
                    bitmap = BitmapFactory.decodeStream(in);
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap",bitmap);
                    msg.setData(b);
                    in.close();
                }
                catch (IOException e1){
                    e1.printStackTrace();
                }
                messageHandler.sendMessage(msg);
            }
        }.start();
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

    private Handler messageHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            ImageView img = (ImageView) findViewById(R.id.gambar);
            img.setImageBitmap((Bitmap) (msg.getData().getParcelable("bitmap")));
            progressDialog.dismiss();
        }
    };
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
