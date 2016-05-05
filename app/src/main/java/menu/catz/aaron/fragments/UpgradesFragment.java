package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import menu.catz.aaron.catzmain.MainActivity;
import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class UpgradesFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{
    Controller control;
    View rootView;
    int nSBProgress;
    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_upgrades, container,false);

        Button btnHP = (Button) rootView.findViewById(R.id.btnHP);
        Button btnView = (Button) rootView.findViewById(R.id.btnView);
        Button btnRange = (Button) rootView.findViewById(R.id.btnRange);
        Button btnXP = (Button) rootView.findViewById(R.id.btnXP);
        Button btnDmg = (Button) rootView.findViewById(R.id.btnDmg);
        Button btnRoF = (Button) rootView.findViewById(R.id.btnRoF);
        Button btnGoldtoXp = (Button) rootView.findViewById(R.id.btnGoldtoXp);
        TextView txtGoldtoXp = (TextView) rootView.findViewById(R.id.txtGoldtoXpCost);
        SeekBar sbGoldtoXp = (SeekBar) rootView.findViewById(R.id.sbGoldtoXp);

        sbGoldtoXp.setOnSeekBarChangeListener(this);
        sbGoldtoXp.setMax(1000);

        btnHP.setOnClickListener(btnListener);
        btnView.setOnClickListener(btnListener);
        btnRange.setOnClickListener(btnListener);
        btnXP.setOnClickListener(btnListener);
        btnDmg.setOnClickListener(btnListener);
        btnRoF.setOnClickListener(btnListener);
        btnGoldtoXp.setOnClickListener(btnListener);

        return rootView;



    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btnHP:
                    break;
                case R.id.btnView:
                    break;
                case R.id.btnDmg:
                    break;
                case R.id.btnXP:
                    break;
                case R.id.btnRange:
                    break;
                case R.id.btnRoF:
                    break;
                case R.id.btnGoldtoXp:
                    //control.player.cash -= nSBProgress;
                    //control.player.EXP += (nSBProgress/control.player.Level);
                    break;
                default:
                    break;
            }
        }
    };
    public void setControl (Controller _CONTROL) {
        control = _CONTROL;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        nSBProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
