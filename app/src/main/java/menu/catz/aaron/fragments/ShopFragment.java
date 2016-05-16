package menu.catz.aaron.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import menu.catz.aaron.catzmain.JSONLoader;
import menu.catz.aaron.catzmain.R;
import menu.catz.aaron.controller.Controller;

public class ShopFragment extends Fragment {

    private ArrayList<Integer> imageId, price, damage;
    private ArrayList<Double> RoF, range;
    private ArrayList<String> name;
    private ArrayList<Bitmap> img;
    private ImageView ivTurret;
    private TextView txtName,txtPrice, txtDes, txtCash;
    private int index = 0;
    private Controller control;
    private Context context;

    //Creates the view of the fragment from the proper XML file in layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop, container,false);

        Button btnNext = (Button) rootView.findViewById(R.id.btnNext);
        Button btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        Button btnBuy = (Button) rootView.findViewById(R.id.btnBuy);
        txtName = (TextView) rootView.findViewById(R.id.txtName);
        txtPrice = (TextView) rootView.findViewById(R.id.txtPrice);
        txtDes = (TextView) rootView.findViewById(R.id.txtDes);
        txtCash = (TextView) rootView.findViewById(R.id.txtCash);
        ivTurret = (ImageView) rootView.findViewById(R.id.ivTurret);
        imageId = new ArrayList<>();
        price = new ArrayList<>();
        damage = new ArrayList<>();
        RoF = new ArrayList<>();
        range = new ArrayList<>();
        name = new ArrayList<>();
        img = new ArrayList<>();
        imageId.add(R.drawable.map_icon);
        imageId.add(R.drawable.options_icon);
        imageId.add(R.drawable.shop_icon);
        imageId.add(R.drawable.upgrade_icon);
        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
        try {
            load();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        updateInfo(index);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<(imageId.size()-1)){
                    index+=1;
                } else {
                    index = 0;
                }
                updateInfo(index);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index>0){
                    index-=1;
                } else {
                    index=(imageId.size()-1);
                }
                updateInfo(index);
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.newTurret(name.get(index), damage.get(index), range.get(index), price.get(index), RoF.get(index));
                txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
            }
        });

        return rootView;
    }

    public void cashCheck () {
        txtCash.setText("Cash: $" + String.valueOf(control.player.cash));
    }

    private void load() throws FileNotFoundException, JSONException {
        String jsonString = JSONLoader.parseFileToString(context, "Turrets.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray turrets = obj.getJSONArray("Turrets");
        for (int i = 0; i < turrets.length(); ++i) {
            obj = turrets.getJSONObject(i);
            damage.add(obj.getInt("Damage"));
            RoF.add(obj.getDouble("RoF"));
            range.add(obj.getDouble("Range"));
            name.add(obj.getString("Name"));
            price.add(obj.getInt("Cost"));
            try {
                URL url = new URL(obj.getString("URL"));
                img.add(BitmapFactory.decodeStream(url.openStream()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //img.add(getBitmapFromURL(obj.getString("URL")));
        }
    }
    private void updateInfo(int index) {
        //ivTurret.setImageResource(imageId.get(index));
        ivTurret.setImageBitmap(img.get(index));
        txtName.setText(name.get(index));
        txtPrice.setText("$" + price.get(index));
        txtDes.setText("RoF: " + String.valueOf(RoF.get(index)) + "\n Damage: " + String.valueOf(damage.get(index)) + "\n Range: " + range.get(index));
    }

    public void setInfo (Controller _CONTROL,Context _CONTEXT) {
        context = _CONTEXT;
        control = _CONTROL;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}