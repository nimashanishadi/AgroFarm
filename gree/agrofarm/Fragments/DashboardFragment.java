package com.example.agrofarm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrofarm.Interface.IFirebaseLoadDone;
import com.example.agrofarm.Models.Crop;
import com.example.agrofarm.Models.Post;
import com.example.agrofarm.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements  IFirebaseLoadDone {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Firebase url;
    IFirebaseLoadDone iFirebaseLoadDone;
    SearchableSpinner searchableSpinner;
    List<Crop> cropList;

    TextView temparature,soil_mos,light,humidity;
    Button setcondition;
    boolean isFirstTimeClicked = true;


    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);




        // Inflate the layout for this fragment
        ////////////////////////////////////////from kalin code start
        //View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //Spinner spinner = (Spinner) fragmentView.findViewById(R.id.spinner);
        // searchableSpinner = (SearchableSpinner) fragmentView.findViewById(R.id.spinner2);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);
        //////////////////////////////from kalin code end

        searchableSpinner = (SearchableSpinner) fragmentView.findViewById(R.id.spinner2);
        myRef = FirebaseDatabase.getInstance().getReference("Conditions");
        iFirebaseLoadDone = this;

        //get data


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Crop> cropList =new ArrayList<>() ;
                for (DataSnapshot cropSnapshot:dataSnapshot.getChildren()){
                    cropList.add(cropSnapshot.getValue(Crop.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(cropList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
        //////////////////////////////////////////////
        temparature = (TextView)  fragmentView.findViewById(R.id.temp);
        soil_mos = (TextView)  fragmentView.findViewById(R.id.soil_mos);
        humidity = (TextView)  fragmentView.findViewById(R.id.humid);
        light = (TextView)  fragmentView.findViewById(R.id.light);
        setcondition = fragmentView.findViewById(R.id.button3);

        setcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"button pressed",Toast.LENGTH_LONG).show();
            }
        });




        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //first time click
                if (!isFirstTimeClicked){
                    String name = cropList.get(i).getCropname();
                    String type = cropList.get(i).getCroptype();
                    String tem = cropList.get(i).getTemparature();
                    String hum = cropList.get(i).getHumidity();
                    String soil = cropList.get(i).getSoilmosture();
                    String lig = cropList.get(i).getLightlevel();

                    temparature.setText(tem);
                    soil_mos.setText(soil);
                    humidity.setText(hum);
                    light.setText(lig);

                    //Crop crop = cropList.get(i);
                    //crop.setTemparature("50");
                    //temparature.setText(crop.getCropname());

                    //String name0 = crop.getCropname();
                    //String name1 = crop.getCroptype();
                    //String name2 = crop.getCropKey();
                    //String name3 = crop.getTemparature();





                    //////////////////////////////////////////////tem+soil+hum+lig+name+type
                    Toast.makeText(getActivity(),"You Selected "+name+" belongs to "+type+" Catagory" ,Toast.LENGTH_LONG).show();
                }
                else {
                    isFirstTimeClicked = false;
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {



            }
        });
        //////////////////////////////////////////////





        //return inflater.inflate(R.layout.fragment_dashboard, container, false);
        return fragmentView;



    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onFirebaseLoadSuccess(List<Crop> CropList) {
        cropList = CropList;
        List<String> name_list = new ArrayList<>();
        for (Crop crop:cropList){
            name_list.add(crop.getCropname());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1,name_list);
            searchableSpinner.setAdapter(adapter);

        }
    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
