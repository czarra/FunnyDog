package com.example.rad.funnydog.fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.example.rad.funnydog.R;
import com.example.rad.funnydog.api.ApiClient;
import com.example.rad.funnydog.data.Dogs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rad on 2017-12-04.
 */

public class DogsFragments extends Fragment {

    private OnFragmentInteractionListener listener;
    private MyDogsRecyclerViewAdapter adapter;
    private Context context;
    private RetrieveDogsTask retrieveDogsTask;
    RecyclerView recyclerView;
    private ProgressBar progressBar1;
    private int selectedSpinner = 1;
    private Button buttonRun;
    private List<Dogs> list_dogs = new ArrayList<>();
    
    public static DogsFragments newInstance() {
        DogsFragments fragment = new DogsFragments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dogs, container, false);
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        listener = (OnFragmentInteractionListener) context;
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        buttonRun = (Button) view.findViewById(R.id.buttonRun) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e("position", position+"  aa");
                switch (position) {
                    default:
                        selectedSpinner = position+1;

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
        for(int i = 1 ;i<= 20; i++){
            categories.add(""+i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,R.layout.simple_spinner_item, categories);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        adapter = new MyDogsRecyclerViewAdapter(new ArrayList<Dogs>(), listener);

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
                list_dogs.clear();
                for (int i = 0; i < list.size(); i++) {
                    adapter.dogs.add(list.get(i));
                    list_dogs.add(list.get(i));
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
    public void onResume(){
        super.onResume();
        Log.d("Restart fragment","Dogs");
        if(list_dogs.size()!=0) {
            retrieveDogsTask = new RetrieveDogsTask() {
                @Override
                protected void onPreExecute() {
                    progressBar1.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                @Override
                protected void onPostExecute(List<Dogs> list) {
                    adapter.dogs.clear();
                    for (int i = 0; i <  list_dogs.size(); i++) {
                        adapter.dogs.add(list_dogs.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    progressBar1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            };

            retrieveDogsTask.execute();
        }
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
            String url = "https://api.thedogapi.co.uk/v2/dog.php?limit=";
            url +=selectedSpinner;

            try {
                Log.d("url",url);
                String jsonResponse = client.getURL(url, String.class);
                Log.d("jsonResponse",jsonResponse);
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray dogs = jsonObject.getJSONArray("data");

                for (int i = 0; i < dogs.length(); i++) {
                    JSONObject articlesObject = dogs.getJSONObject(i);
                    Dogs item = Dogs.fromJsonObject(articlesObject);

                    if(item!=null) {
                        Log.d("Respose Dog",item.toString());
                        list.add(item);
                    }
                }
                return list;
            } catch (Exception exp) {
                Log.e("Articles error",exp.getMessage());
            }
            return new ArrayList<>();
        }


    }
}
