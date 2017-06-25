package hr.apps.maltar.simongame;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.lang.reflect.Field;

import hr.apps.maltar.simongame.game.Game;

import static hr.apps.maltar.simongame.StartScreen.setHighScore;

public class game_activity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static MediaPlayer mediaPlayer0;
    private static MediaPlayer mediaPlayer1;
    private static MediaPlayer mediaPlayer2;
    private static MediaPlayer mediaPlayer3;
    private static MediaPlayer mediaPlayerFail;

    public static Context context;

    public static View block0;
    public static View block1;
    public static View block2;
    public static View block3;

    private static TextView scoreTextView;

    private static CountDownTimer newGameTimer;
    private static CountDownTimer block0Timer;
    private static CountDownTimer block1Timer;
    private static CountDownTimer block2Timer;
    private static CountDownTimer block3Timer;

    private static Game game;

    private static boolean isMediaPlayerRelease = true;

    private float volume = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_activity);
        context = getApplicationContext();

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

        scoreTextView = (TextView) findViewById(R.id.score_text_view);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("sound", true)) {
            setMediaPlayers();
            setMediaPlayersVolume(volume);
        } else {
            setMediaPlayers();
            setMediaPlayersVolume(0);
        }

        isMediaPlayerRelease = false;

        game = new Game();

        View.OnClickListener blockOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushBlock(v.getId());
                game.playerMove(v.getId());
            }
        };

        block0 = findViewById(R.id.block_0);
        block0.setOnClickListener(blockOnClickListener);
        block1 = findViewById(R.id.block_1);
        block1.setOnClickListener(blockOnClickListener);
        block2 = findViewById(R.id.block_2);
        block2.setOnClickListener(blockOnClickListener);
        block3 = findViewById(R.id.block_3);
        block3.setOnClickListener(blockOnClickListener);

        setViewsClickable(false);

        newGameTimer = new CountDownTimer(2000,500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                game.startGame();
            }
        };
        block0Timer = new CountDownTimer(500, 500) {
            @Override
            public void onFinish() {
                block0.setBackgroundColor(ContextCompat.getColor(context, R.color.greenOff));
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        };
        block1Timer = new CountDownTimer(500, 500) {
            @Override
            public void onFinish() {
                block1.setBackgroundColor(ContextCompat.getColor(context, R.color.redOff));
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        };
        block2Timer = new CountDownTimer(500, 500) {
            @Override
            public void onFinish() {
                block2.setBackgroundColor(ContextCompat.getColor(context, R.color.yellowOff));
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        };
        block3Timer = new CountDownTimer(500, 500) {
            @Override
            public void onFinish() {
                block3.setBackgroundColor(ContextCompat.getColor(context, R.color.blueOff));
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        };

        newGameTimer.start();
    }

    public static void scoreViewUpdate(int score) {
        if (score > sharedPreferences.getInt("score", 0)) {
            editor.putInt("score", score);
            editor.commit();
            setHighScore();
        }
        scoreTextView.setText("SCORE:  " + score);
    }

    public static void playBlock0() {
        if (!isMediaPlayerRelease) {
            if (mediaPlayer0.isPlaying()) {
                mediaPlayer0.seekTo(0);
            }
            mediaPlayer0.start();
        }
    }

    public static void playBlock1() {
        if (!isMediaPlayerRelease) {
            if (mediaPlayer1.isPlaying()) {
                mediaPlayer1.seekTo(0);
            }
            mediaPlayer1.start();
        }
    }

    public static void playBlock2() {
        if (!isMediaPlayerRelease) {
            if (mediaPlayer2.isPlaying()) {
                mediaPlayer2.seekTo(0);
            }
            mediaPlayer2.start();
        }
    }

    public static void playBlock3() {
        if (!isMediaPlayerRelease) {
            if (mediaPlayer3.isPlaying()) {
                mediaPlayer3.seekTo(0);
            }
            mediaPlayer3.start();
        }
    }

    public static void pushBlock(int id) {
        switch (id) {
            case R.id.block_0:
                playBlock0();
                block0.setBackgroundColor(ContextCompat.getColor(context, R.color.greenOn));
                block0Timer.start();
                break;
            case R.id.block_1:
                playBlock1();
                block1.setBackgroundColor(ContextCompat.getColor(context, R.color.redOn));
                block1Timer.start();
                break;
            case R.id.block_2:
                playBlock2();
                block2.setBackgroundColor(ContextCompat.getColor(context, R.color.yellowOn));
                block2Timer.start();
                break;
            case R.id.block_3:
                playBlock3();
                block3.setBackgroundColor(ContextCompat.getColor(context, R.color.blueOn));
                block3Timer.start();
                break;
        }
    }

    public static void pushBlockAuto(int id) {
        pushBlock(id);
        //nextBlockTimer.start();
    }

    private static void setMediaPlayers() {
        mediaPlayer0 = MediaPlayer.create(context, R.raw.block0);
        mediaPlayer1 = MediaPlayer.create(context, R.raw.block1);
        mediaPlayer2 = MediaPlayer.create(context, R.raw.block2);
        mediaPlayer3 = MediaPlayer.create(context, R.raw.block3);
        mediaPlayerFail = MediaPlayer.create(context, R.raw.player_failed);
    }

    public static void playPlayerFailedMedia() {
        mediaPlayerFail.start();
    }

    public static void setViewsClickable(boolean clickable) {
        block0.setClickable(clickable);
        block1.setClickable(clickable);
        block2.setClickable(clickable);
        block3.setClickable(clickable);
    }

    private void setMediaPlayersVolume(float volume) {
        mediaPlayer0.setVolume(volume, volume);
        mediaPlayer1.setVolume(volume, volume);
        mediaPlayer2.setVolume(volume, volume);
        mediaPlayer3.setVolume(volume, volume);
        mediaPlayerFail.setVolume(volume, volume);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        if (sharedPreferences.getBoolean("sound", false)) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_volume_high));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_volume_off));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_volume:
                if (sharedPreferences.getBoolean("sound", true)) {
                    editor.putBoolean("sound", false);
                    editor.commit();
                    item.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_volume_off));
                    setMediaPlayersVolume(0);
                } else {
                    editor.putBoolean("sound", true);
                    editor.commit();
                    item.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_volume_high));
                    setMediaPlayersVolume(volume);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer0 != null) mediaPlayer0.release();
        if (mediaPlayer1 != null) mediaPlayer1.release();
        if (mediaPlayer2 != null) mediaPlayer2.release();
        if (mediaPlayer3 != null) mediaPlayer3.release();
        if (mediaPlayerFail != null) mediaPlayerFail.release();
        isMediaPlayerRelease = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer0 != null) mediaPlayer0.release();
        if (mediaPlayer1 != null) mediaPlayer1.release();
        if (mediaPlayer2 != null) mediaPlayer2.release();
        if (mediaPlayer3 != null) mediaPlayer3.release();
        if (mediaPlayerFail != null) mediaPlayerFail.release();
        isMediaPlayerRelease = true;

        if (newGameTimer != null) newGameTimer.cancel();
        if ((block0Timer != null)) block0Timer.cancel();
        if ((block1Timer != null)) block1Timer.cancel();
        if ((block2Timer != null)) block2Timer.cancel();
        if ((block3Timer != null)) block3Timer.cancel();

        block0.setBackgroundColor(ContextCompat.getColor(context, R.color.greenOff));
        block1.setBackgroundColor(ContextCompat.getColor(context, R.color.redOff));
        block2.setBackgroundColor(ContextCompat.getColor(context, R.color.yellowOff));
        block3.setBackgroundColor(ContextCompat.getColor(context, R.color.blueOff));

        game.stopTimers();
    }
}
