package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class ShopFragment extends Fragment {

    private int nTurretnum = 4;
    private int arnImageId[] = new int[nTurretnum];
    private String arsTurretname[] = new String[nTurretnum];
    private int arnPrice[] = new int[nTurretnum];
    private int arnDamage[] = new int[nTurretnum];
    private int arnRange[] = new int[nTurretnum];
    private double ardRoF[] = new double[nTurretnum];
    private ImageView ivTurret;
    private TextView txtTname,txtPrice,txtDes;
    private int nTurretL = 0;
    Controller control;
    Context context;
    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop, container,false);

        Button btnNext = (Button) rootView.findViewById(R.id.btnNext);
        Button btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        Button btnBuy = (Button) rootView.findViewById(R.id.btnBuy);
        txtTname = (TextView) rootView.findViewById(R.id.txtName);
        txtPrice = (TextView) rootView.findViewById(R.id.txtPrice);
        txtDes = (TextView) rootView.findViewById(R.id.txtDes);
        ivTurret = (ImageView) rootView.findViewById(R.id.ivTurret);
        arnImageId[0] = R.drawable.map_icon;
        arnImageId[1] = R.drawable.options_icon;
        arnImageId[2] = R.drawable.shop_icon;
        arnImageId[3] = R.drawable.upgrade_icon;

        try {
            load();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

        Updateinfo(nTurretL);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nTurretL<(arnImageId.length-1)){
                    nTurretL+=1;
                } else {
                    nTurretL = 0;
                }
                Updateinfo(nTurretL);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nTurretL>0){
                    nTurretL-=1;
                } else {
                    nTurretL=(arnImageId.length-1);
                }
                Updateinfo(nTurretL);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.newTurret(arsTurretname[nTurretL]);
            }
        });

        return rootView;

    }

    private void load() throws FileNotFoundException, JSONException {
        String jsonString = JSONLoader.parseFileToString(context, "Turrets.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray turrets = obj.getJSONArray("Turrets");
        for (int i = 0; i < turrets.length(); ++i) {
            obj = turrets.getJSONObject(i);
            arnDamage[i] = obj.getInt("Damage");
            ardRoF[i] = obj.getDouble("RoF");
            arnRange[i] = obj.getInt("Range");
            arsTurretname[i] = obj.getString("Name");
            arnPrice[i] = obj.getInt("Cost");
        }
    }
    private void Updateinfo(int nTurretL) {
        ivTurret.setImageResource(arnImageId[nTurretL]);
        txtTname.setText(arsTurretname[nTurretL]);
        txtPrice.setText("$" + String.valueOf(arnPrice[nTurretL]));
    }

    public void setInfo (Controller _CONTROL,Context _CONTEXT) {
        context = _CONTEXT;
        control = _CONTROL;
    }
}