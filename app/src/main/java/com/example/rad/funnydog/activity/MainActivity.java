package com.example.rad.funnydog.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.data.Dogs;
import com.example.rad.funnydog.fragments.DogsFragments;

public class MainActivity extends AppCompatActivity  implements DogsFragments.OnFragmentInteractionListener {

    private SQLiteDatabase myDataBase;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Dog( ID integer primary key autoincrement,ID_DOG VARCHAR,URL VARCHAR, TIME INT, FORMAT VARCHAR);";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDataBase = openOrCreateDatabase("Dogs", MODE_PRIVATE, null);
        myDataBase.execSQL(CREATE_TABLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFragment(DogsFragments.newInstance());
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


        if (id == R.id.start) {
            //Intent mIntent = new Intent(MainActivity.this, CategoryActivity.class);
           // mIntent.putExtra("category", "katalog");
            //startActivity(mIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Dogs item) {

        try {
            String query = "SELECT * FROM Dog WHERE ID_DOG LIKE '"+item.id+"'";
            Cursor cursor = myDataBase.rawQuery(query, null);
            Log.e("in base",""+cursor.getCount());
            CharSequence text ="";
            if(cursor.getCount()>0){
                myDataBase.execSQL("DELETE FROM Dog WHERE ID_DOG LIKE '"+item.id+"'");
                text = "Usunięto z ulubionych";

            }else {
                myDataBase.execSQL("INSERT INTO Dog (ID_DOG ,URL , TIME, FORMAT ) VALUES('" +
                        item.id + "','" +
                        item.url + "','" +
                        System.currentTimeMillis() + "','" +
                        item.format + "');");
                text = "Dodano do ulubionych";
            }
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
           // while (cursor.moveToNext()) {
               //cursor.getString(cursor.getColumnIndex("allFriends"));
            //}
        }catch(Exception ex){
            Log.e("select","Erro in geting id "+ex.toString());
        }
    }
}
