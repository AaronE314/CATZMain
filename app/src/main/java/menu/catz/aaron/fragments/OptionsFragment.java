package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import menu.catz.aaron.catzmain.FileHandler;
import menu.catz.aaron.catzmain.JSONLoader;
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.cities_array, android.R.layout.simple_spinner_item);
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
                saveData();
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
                saveData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        return rootView;

    }
    private void saveData () {
        Double lat = Double.NaN, lng = Double.NaN;
        int num = -1;
        try {
            JSONObject obj = new JSONObject(new JSONLoader().parseFileToString(control.context, "Coordinates.json"));
            JSONArray ar = obj.getJSONArray("Locations");
            for (int i = 0; i < ar.length(); i++) {
                if (ar.getJSONObject(i).getString("Name").equals(sSelectedMock)) {
                    lat = ar.getJSONObject(i).getDouble("Lat");
                    lng = ar.getJSONObject(i).getDouble("Long");
                }
            }
            obj = new JSONObject(new JSONLoader().parseFileToString(control.context, "MapType.json"));
            ar = obj.getJSONArray("Types");
            for (int i = 0; i < ar.length(); i++) {
                if (ar.getJSONObject(i).getString("Name").equals(sSelectedMap)) {
                    num = ar.getJSONObject(i).getInt("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        control.writeToId(new FileHandler(control.context, "data.txt").readFromFile(), cbMock.isChecked() + " " + lat + " " + lng + " " + num);
    }

    public void setInfo(Controller _CONTROL, Context _CONTEXT) {
        context = _CONTEXT;
        control = _CONTROL;
    }
}