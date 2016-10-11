package alone.fb_login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alone on 12/29/2015.
 */
public class map_marker extends Activity
{
    private GoogleMap mMap;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta);

        checknternetConnection();
        GetData dataambil = new GetData();
        dataambil.kata_tunggu = "Loading";
        dataambil.execute();

        //setUpMap();

        //plotMarkers(mMyMarkersArray);
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
        ProgressDialog dialog = new ProgressDialog(map_marker.this);
        String kata_tunggu;
        String hasilData;
        @Override
        protected String doInBackground(String... params) {
            String urlnya = "http://192.168.137.1:81/tambalban/getdata.php";
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
            mMarkersHashMap = new HashMap<Marker, MyMarker>();
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(hasilData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            JSONObject json = new JSONObject(hasilData);
            for(int i = 0; i < jsonarray.length(); i++) {
                JSONObject json = null;
                try {
                    json = jsonarray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tbnama = null;
                try {
                    tbnama = json.getString("tb_nama");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tblat = null;
                try {
                    tblat = json.getString("tb_lat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String tblon = null;
                try {
                    tblon = json.getString("tb_lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMyMarkersArray.add(new MyMarker(tbnama, "icon2", Double.parseDouble(tblat),Double.parseDouble(tblon)));
                //Toast.makeText(map_marker.this,tblat,Toast.LENGTH_LONG).show();

            }


            setUpMap();

            plotMarkers(mMyMarkersArray);
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

    private void plotMarkers(ArrayList<MyMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (MyMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private int manageMarkerIcon(String markerIcon)
    {
        if (markerIcon.equals("icon1"))
            return R.drawable.icon1;
        else if(markerIcon.equals("icon2"))
            return R.drawable.icon2;
        else if(markerIcon.equals("icon3"))
            return R.drawable.icon3;
        else if(markerIcon.equals("icon4"))
            return R.drawable.icon4;
        else if(markerIcon.equals("icon5"))
            return R.drawable.icon5;
        else if(markerIcon.equals("icon6"))
            return R.drawable.icon6;
        else if(markerIcon.equals("icon7"))
            return R.drawable.icon7;
        else
            return R.drawable.icondefault;
    }


    private void setUpMap()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.

            if (mMap != null)
            {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker)
                    {
                        marker.showInfoWindow();
                        return true;
                    }
                });
            }
            else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            MyMarker myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            TextView anotherLabel = (TextView)v.findViewById(R.id.another_label);

            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

            markerLabel.setText(myMarker.getmLabel());
            anotherLabel.setText("A custom text");

            return v;
        }
    }
}
