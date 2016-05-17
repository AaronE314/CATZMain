package menu.catz.aaron.catzmain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        Button btnLoad = (Button) findViewById(R.id.btnLoad);
        Button btnNew = (Button) findViewById(R.id.btnNew);
    }
}
