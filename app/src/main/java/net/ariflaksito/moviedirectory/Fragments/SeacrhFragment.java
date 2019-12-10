package net.ariflaksito.moviedirectory.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ariflaksito.moviedirectory.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeacrhFragment extends Fragment {


    public SeacrhFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_seacrh, container, false);
    }

}
