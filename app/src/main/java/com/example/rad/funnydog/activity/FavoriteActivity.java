package com.example.rad.funnydog.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.data.Dogs;
import com.example.rad.funnydog.fragments.DogsFragments;
import com.example.rad.funnydog.fragments.MyDogsFragments;

public class FavoriteActivity extends AppCompatActivity  implements MyDogsFragments.OnFragmentInteractionListener {

    private SQLiteDatabase myDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        loadFragment(MyDogsFragments.newInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_favorite, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Dogs item) {

        try {
            myDataBase = openOrCreateDatabase("Dogs", MODE_PRIVATE, null);
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
            myDataBase.close();
        }catch(Exception ex){
            Log.e("select","Erro in geting id "+ex.toString());
        }
    }
}
