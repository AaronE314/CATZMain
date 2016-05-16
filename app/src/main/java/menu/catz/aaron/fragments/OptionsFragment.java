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
    private String sSelectedMock, sSelectedMap;
    private Spinner spMapType,spMock;
    private CheckBox cbMock;
    MainActivity mainActivity = new MainActivity();
    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_options, container,false);

        cbMock = (CheckBox) rootView.findViewById(R.id.cbMock);

        spMapType = (Spinner) rootView.findViewById(R.id.spMapType);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.MapType_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMapType.setAdapter(adapter2);
        spMock = (Spinner) rootView.findViewById(R.id.spMock);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMock.setAdapter(adapter);

        spMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sSelectedMap = spMapType.getSelectedItem().toString();
                ChangeMapType();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spMock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(cbMock.isChecked()) {
                    sSelectedMock = spMock.getSelectedItem().toString();
                } else { sSelectedMock = null; }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        return rootView;

    }
    public void setInfo (Controller _CONTROL,Context _CONTEXT) {
        context = _CONTEXT;
        control = _CONTROL;
    }

    public void ChangeMapType(){
        switch (sSelectedMap) {
            case "Hybrid":
                //return GoogleMap.MAP_TYPE_HYBRID;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_HYBRID));
            case "Terrain":
                //return GoogleMap.MAP_TYPE_TERRAIN;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_TERRAIN));
            case "None":
                //return GoogleMap.MAP_TYPE_NONE;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_NONE));
            case "Normal":
                //return GoogleMap.MAP_TYPE_NORMAL;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_NORMAL));
            case "Satellite":
                //return GoogleMap.MAP_TYPE_SATELLITE;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_SATELLITE));
            default:
                //return GoogleMap.MAP_TYPE_NORMAL;
                //JSONLoader.parseStringToFile(context, "MapType.json", String.valueOf(GoogleMap.MAP_TYPE_NORMAL));
        }
    }
}