package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class UpgradesFragment extends Fragment {
    Controller control;
    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upgrades, container,false);

        return rootView;

    }
    public void setControl (Controller _CONTROL) {
        control = _CONTROL;
    }
}
