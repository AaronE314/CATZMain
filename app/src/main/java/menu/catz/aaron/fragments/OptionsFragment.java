package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.maps.GoogleMap;

import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.MainActivity;
import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class OptionsFragment extends Fragment {
    Controller control;
    Context context;
    private Spinner spMapType, spMock;
    private String sSelectedMock, sSelectedMap;
    private CheckBox cbMock;
    private Button btnSave;

    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_options, container, false);

        cbMock = (CheckBox) rootView.findViewById(R.id.cbMock);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        spMapType = (Spinner) rootView.findViewById(R.id.spMapType);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.MapType_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMapType.setAdapter(adapter2);
        spMock = (Spinner) rootView.findViewById(R.id.spMock);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMock.setAdapter(adapter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {control.Save();
            }
        });

        spMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sSelectedMap = spMapType.getSelectedItem().toString();
                //TODO Save Setting to drive
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spMock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (cbMock.isChecked()) {
                    sSelectedMock = spMock.getSelectedItem().toString();
                } else {
                    sSelectedMock = "Hybrid";
                }
                //TODO Save Setting to drive
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        return rootView;

    }

    private AdapterView.OnItemSelectedListener btnListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void setInfo(Controller _CONTROL, Context _CONTEXT) {
        context = _CONTEXT;
        control = _CONTROL;
    }
}