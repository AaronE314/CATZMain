package menu.catz.aaron.fragments;

import android.app.Fragment;
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
import java.io.FileReader;
import java.util.Scanner;

import menu.catz.aaron.catzmain.R;

public class ShopFragment extends Fragment {

    int nTurretnum = 4;
    int arnImageId[] = new int[nTurretnum];
    String arsTurretname[] = new String[nTurretnum];
    int arnPrice[] = new int[nTurretnum];
    int arnDamage[] = new int[nTurretnum];
    int arnRange[] = new int[nTurretnum];
    double ardRoF[] = new double[nTurretnum];
    ImageView ivTurret;
    TextView txtTname,txtPrice;
    int nTurretL = 0;
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
        ivTurret = (ImageView) rootView.findViewById(R.id.ivTurret);
        arnImageId[0] = R.drawable.map_icon;
        arnImageId[1] = R.drawable.options_icon;
        arnImageId[2] = R.drawable.shop_icon;
        arnImageId[3] = R.drawable.upgrade_icon;
        /*
        arsTurretname[0] = "Turret 1";
        arsTurretname[1] = "Turret 2";
        arsTurretname[2] = "Turret 3";
        arsTurretname[3] = "Turret 4";*/
        try {
            load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

        return rootView;

    }

    private void load() throws FileNotFoundException, JSONException {
        String jsonString = "{" + "  \"Turrets\": [" + "    {" + "      \"Id\": \"1\"," + "      \"Damage\": \"100\"," + "      \"Range\": \"1.01\"," + "      \"RoF\": \"1.01\"," + "      \"Cost\": \"100\"," + "      \"Name\": \"ONE\"" + "    }," + "    {" + "      \"Id\": \"2\"," + "      \"Damage\": \"200\"," + "      \"Range\": \"2.02\"," + "      \"RoF\": \"2.02\"," + "      \"Cost\": \"200\"," + "      \"Name\": \"TWO\"" + "    }," + "    {" + "      \"Id\": \"3\"," + "      \"Damage\": \"300\"," + "      \"Range\": \"3.03\"," + "      \"RoF\": \"3.03\"," + "      \"Cost\": \"300\"," + "      \"Name\": \"THREE\"" + "    }," + "    {" + "      \"Id\": \"4\"," + "      \"Damage\": \"400\"," + "      \"Range\": \"4.04\"," + "      \"RoF\": \"4.04\"," + "      \"Cost\": \"400\"," + "      \"Name\": \"FOUR\"" + "    }" + "  ]" + "}";
        /*Scanner fin = new Scanner(new FileReader("Turrets.json"));
        while (fin.hasNextLine()) {
            jsonString += fin.nextLine();
        }*/
        JSONObject obj = new JSONObject(jsonString);
        JSONArray turrets = obj.getJSONArray("Turrets");
        for (int i = 0; i < turrets.length(); ++i) {
            obj = turrets.getJSONObject(i);
            arnDamage[i] = obj.getInt("Damage");
            ardRoF[i] = obj.getDouble("RoF");
            arnRange[i] = obj.getInt("Range");
            arsTurretname[i] = obj.getString("Name");
        }
    }
    public void Updateinfo(int nTurretL) {
        ivTurret.setImageResource(arnImageId[nTurretL]);
        txtTname.setText(arsTurretname[nTurretL]);
        //txtPrice.setText(arnPrice[nTurretL]);
    }

}