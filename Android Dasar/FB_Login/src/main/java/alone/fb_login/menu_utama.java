package alone.fb_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by alone on 12/28/2015.
 */
public class menu_utama extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        ImageButton map = (ImageButton) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO Auto-generated method stub
                Intent baru = new Intent(menu_utama.this, map_marker.class);
                startActivity(baru);
            }
        });
        ImageButton feed = (ImageButton) findViewById(R.id.feed);

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO Auto-generated method stub
                Intent baru = new Intent(menu_utama.this, Feedback.class);
                startActivity(baru);
            }
        });

        ImageButton add = (ImageButton) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO Auto-generated method stub
                Intent baru = new Intent(menu_utama.this, addLocation.class);
                startActivity(baru);
            }
        });
        ImageButton about = (ImageButton) findViewById(R.id.about);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO Auto-generated method stub
                Intent baru = new Intent(menu_utama.this, about.class);
                startActivity(baru);
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
