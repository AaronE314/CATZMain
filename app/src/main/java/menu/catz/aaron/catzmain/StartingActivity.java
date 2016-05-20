package menu.catz.aaron.catzmain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StartingActivity extends AppCompatActivity {

    private boolean newGame = false;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);

        Button btnLoad = (Button) findViewById(R.id.btnLoad);
        Button btnNew = (Button) findViewById(R.id.btnNew);

        //Learned passing data to other activities from Dina's MVC
        assert btnLoad != null;
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame = false;
                Bundle basket = new Bundle();
                basket.putBoolean("NewGame", newGame);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });

        assert btnNew != null;
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame = true;
                Bundle basket = new Bundle();
                basket.putBoolean("NewGame", newGame);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });
    }
}
