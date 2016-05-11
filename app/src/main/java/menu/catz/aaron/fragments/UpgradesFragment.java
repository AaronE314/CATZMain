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
    private Controller control;
    private View rootView;
    private TextView txtCashtoXp, txtCash;
    private int nSBProgress;
    private SeekBar sbGoldtoXp;

    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_upgrades, container, false);

        Button btnHP = (Button) rootView.findViewById(R.id.btnHP);
        Button btnView = (Button) rootView.findViewById(R.id.btnView);
        Button btnRange = (Button) rootView.findViewById(R.id.btnRange);
        Button btnXP = (Button) rootView.findViewById(R.id.btnXP);
        Button btnDmg = (Button) rootView.findViewById(R.id.btnDmg);
        Button btnRoF = (Button) rootView.findViewById(R.id.btnRoF);
        Button btnCashtoXp = (Button) rootView.findViewById(R.id.btnCashtoXp);
        txtCashtoXp = (TextView) rootView.findViewById(R.id.txtCashtoXpCost);
        txtCash = (TextView) rootView.findViewById(R.id.txtCash);
        sbGoldtoXp = (SeekBar) rootView.findViewById(R.id.sbGoldtoXp);

        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
        sbGoldtoXp.setMax(control.player.cash);
        sbGoldtoXp.setOnSeekBarChangeListener(this);

        btnHP.setOnClickListener(btnListener);
        btnView.setOnClickListener(btnListener);
        btnRange.setOnClickListener(btnListener);
        btnXP.setOnClickListener(btnListener);
        btnDmg.setOnClickListener(btnListener);
        btnRoF.setOnClickListener(btnListener);
        btnCashtoXp.setOnClickListener(btnListener);

        return rootView;

    }
    public void cashCheck () {
        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
        sbGoldtoXp.setMax(control.player.cash);
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId(), newCost;
            switch (id) {
                case R.id.btnHP:
                    newCost = control.plusHealth();
                    txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnView:
                    newCost = control.plusView();
                        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnDmg:
                    newCost = control.plusDmg();
                        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnXP:
                    newCost = control.plusKills();
                        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnRange:
                    newCost = control.plusRange();
                        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnRoF:
                    newCost = control.plusRoF();
                        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                case R.id.btnCashtoXp:
                    control.plusEXP(nSBProgress);
                    txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
                    break;
                default:
                    break;
            }
        }
    };

    public void setControl(Controller _CONTROL) {
        control = _CONTROL;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        nSBProgress = progress;
        txtCashtoXp.setText("Amount: " + String.valueOf(progress / control.player.Level) +  "\nCost: $" + String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
