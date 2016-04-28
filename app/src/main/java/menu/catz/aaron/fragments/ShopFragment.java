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

import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class ShopFragment extends Fragment {

    int nTurretnum = 4;
    int arnImageId[] = new int[nTurretnum];
    String arsTurretname[] = new String[nTurretnum];
    int arnPrice[] = new int[nTurretnum];
    ImageView ivTurret;
    TextView txtTname,txtPrice;
    int nTurretL = 0;
    Controller control;
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
        arsTurretname[0] = "Turret 1";
        arsTurretname[1] = "Turret 2";
        arsTurretname[2] = "Turret 3";
        arsTurretname[3] = "Turret 4";
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
                control.newTurret();
            }
        });

        return rootView;

    }

    public void Updateinfo(int nTurretL) {
        ivTurret.setImageResource(arnImageId[nTurretL]);
        txtTname.setText(arsTurretname[nTurretL]);
//        txtPrice.setText(arnPrice[nTurretL]);
    }

    public void setControl (Controller _CONTROL) {
        control = _CONTROL;
    }
}