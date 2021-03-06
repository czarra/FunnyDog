package com.example.rad.funnydog.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.api.ApiClient;
import com.example.rad.funnydog.data.Dogs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rad on 2017-12-04.
 */

public class MyDogsFragments extends Fragment {

    private OnFragmentInteractionListener listener;
    private MyFavoriteDogsRecyclerViewAdapter adapter;
    private Context context;
    private RetrieveDogsTask retrieveDogsTask;
    RecyclerView recyclerView;
    private ProgressBar progressBar1;
    private int selectedSpinner = 0;
    private Button buttonRun;
    private SQLiteDatabase myDataBase;
    private TextView empty;
    
    public static MyDogsFragments newInstance() {
        MyDogsFragments fragment = new MyDogsFragments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_dogs, container, false);
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        listener = (OnFragmentInteractionListener) context;
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        buttonRun = (Button) view.findViewById(R.id.buttonRun) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        empty = (TextView)   view.findViewById(R.id.empty) ;

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e("position", position+"  aa");
                switch (position) {
                    default:
                        selectedSpinner = position;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                selectedSpinner=1;
            }
        });
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Data dodania rosnąco");// 0 -rosnaco
        categories.add("Data dodania malejąco");// 1 -manlejąco

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,R.layout.simple_spinner_item, categories);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        adapter = new MyFavoriteDogsRecyclerViewAdapter(new ArrayList<Dogs>(), listener);

        retrieveDogsTask = forButtons();
        retrieveDogsTask.execute();
        recyclerView.setAdapter(adapter);

        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveDogsTask = forButtons();
                retrieveDogsTask.execute();
                recyclerView.scrollToPosition(0);
            }

        });
        return view;
    }

    private RetrieveDogsTask forButtons(){
        RetrieveDogsTask local =  new RetrieveDogsTask() {
            @Override
            protected void onPreExecute() {
                progressBar1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(List<Dogs> list) {
                adapter.dogs.clear();
                for (int i = 0; i < list.size(); i++) {
                    adapter.dogs.add(list.get(i));
                }
                if(list.size()==0){
                    empty.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                progressBar1.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        };

        return local;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Dogs item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    class RetrieveDogsTask extends AsyncTask<String, String, List<Dogs>> {

        private final ApiClient client = ApiClient.getInstance();
        List<Dogs> list = new ArrayList<>();
        @Override
        protected List<Dogs> doInBackground(String... urls) {

            try {
                myDataBase = context.openOrCreateDatabase("Dogs", MODE_PRIVATE, null);
                String query = "SELECT * FROM Dog ";
                if(selectedSpinner==1){
                    query +=" ORDER BY time DESC";
                }else {
                    query +=" ORDER BY time ASC";
                }
                Cursor cursor = myDataBase.rawQuery(query, null);
                Log.e("in base",""+cursor.getCount());

                 while (cursor.moveToNext()) {
                     Dogs item  = new Dogs(""+cursor.getString(1),""+cursor.getString(2),
                             ""+cursor.getString(3),""+cursor.getString(4));
                   //  Log.d("in base in: ",""+cursor.getString(1));// id
                    // Log.d("in base in: ",""+cursor.getString(2));//url
                     //Log.d("in base in: ",""+cursor.getString(3));//time
                    // Log.d("in base in: ",""+cursor.getString(4));//form
                     item.setStar(true);
                     list.add(item);
                }
                myDataBase.close();
                return list;
            }catch(Exception ex){
                Log.e("select","Erro in geting id "+ex.toString());
            }
            return new ArrayList<>();
        }


    }
}
