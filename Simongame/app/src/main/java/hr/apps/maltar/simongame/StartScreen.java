package hr.apps.maltar.simongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

public class StartScreen extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static TextView highScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        highScoreTextView = (TextView) findViewById(R.id.high_score_text_view);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), game_activity.class);
                startActivity(intent);
            }
        });

        setHighScore();
    }

    public static void setHighScore() {
        int score = sharedPreferences.getInt("score", 0);
        highScoreTextView.setText("HIGH SCORE = " + score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_high_score:
                editor.putInt("score", 0);
                editor.commit();
                setHighScore();
                break;
            case  R.id.instructions:
                Intent intent = new Intent(getApplicationContext(), Instructions.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
