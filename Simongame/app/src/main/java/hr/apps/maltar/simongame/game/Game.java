package hr.apps.maltar.simongame.game;

import android.os.CountDownTimer;
import android.widget.Toast;

import java.util.ArrayList;

import hr.apps.maltar.simongame.R;

import static hr.apps.maltar.simongame.game_activity.context;
import static hr.apps.maltar.simongame.game_activity.playPlayerFailedMedia;
import static hr.apps.maltar.simongame.game_activity.pushBlockAuto;
import static hr.apps.maltar.simongame.game_activity.scoreViewUpdate;
import static hr.apps.maltar.simongame.game_activity.setViewsClickable;

/**
 * Created by Maltar on 14.6.2017..
 */

public class Game {
    private int score;
    private int playerMoveCount;
    private int indexOfBlockToShow = 0;
    private CountDownTimer playerWaitTimer;
    private CountDownTimer blockShowTimer;
    private CountDownTimer newBlockTimer;
    private CountDownTimer nextGameTimer;

    private ArrayList<Integer> blockOrderArray = new ArrayList<>();
    private ArrayList<Integer> playerMoveArray = new ArrayList<>();

    public Game() {
        blockShowTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showNextInOrder();
            }
        };
        newBlockTimer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gameProcessContinue();
            }
        };
        nextGameTimer = new CountDownTimer(5500, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startGame();
            }
        };
    }

    public void startGame() {
        scoreViewUpdate(score);
        scoreViewUpdate(score);
        indexOfBlockToShow = 0;
        blockOrderArray.clear();
        playerMoveArray.clear();
        gameProcessContinue();
    }

    private void showNextInOrder() {
        int currentBlock = blockOrderArray.get(indexOfBlockToShow);
        switch (currentBlock) {
            case 0:
                pushBlockAuto(R.id.block_0);
                break;
            case 1:
                pushBlockAuto(R.id.block_1);
                break;
            case 2:
                pushBlockAuto(R.id.block_2);
                break;
            case 3:
                pushBlockAuto(R.id.block_3);
                break;
        }
        if (indexOfBlockToShow == blockOrderArray.size() -1) {
            indexOfBlockToShow = 0;
            newBlockTimer.start();
        } else {
            indexOfBlockToShow++;
            gameProcessShow();
        }
    }

    private void gameProcessShow() {
        //setViewsClickable(false);
        if (indexOfBlockToShow < blockOrderArray.size()) {
            blockShowTimer.start();
        } else {
            gameProcessContinue();
        }
    }

    private void gameProcessContinue() {
        playerMoveCount = -1;
        final int block = getBlock();
        blockOrderArray.add(block);
        switch (block) {
            case 0:
                pushBlockAuto(R.id.block_0);
                break;
            case 1:
                pushBlockAuto(R.id.block_1);
                break;
            case 2:
                pushBlockAuto(R.id.block_2);
                break;
            case 3:
                pushBlockAuto(R.id.block_3);
                break;
        }
        waitForPlayer();
    }

    private void waitForPlayer() {
        setViewsClickable(true);
        waitForPlayerTimer((blockOrderArray.size() + 4) * 500, 500);
    }

    public void playerMove(int id) {
        playerWaitTimer.cancel();
        int block;
        switch (id) {
            case R.id.block_0:
                block = 0;
                break;
            case R.id.block_1:
                block = 1;
                break;
            case R.id.block_2:
                block = 2;
                break;
            case R.id.block_3:
                block = 3;
                break;
            default: block = 0;
        }
        playerMoveCount++;
        if (block != blockOrderArray.get(playerMoveCount)) {
            playerFailed();
        } else if(playerMoveCount == blockOrderArray.size() - 1) {
            setViewsClickable(false);
            new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    gameProcessShow();
                }
            }.start();
            score++;
            scoreViewUpdate(score);
        } else {
            playerMoveArray.add(block);
        }
    }

    private void playerFailed() {
        playPlayerFailedMedia();
        score = 0;
        scoreViewUpdate(score);
        setViewsClickable(false);
        Toast.makeText(context, "YOU FAILED", Toast.LENGTH_LONG).show();
        nextGameTimer.start();
    }

    private int getBlock() {
        return ((Double) (Math.random() * 10)).intValue() % 4;
    }

    private void waitForPlayerTimer(int time, int thick) {
       playerWaitTimer = new CountDownTimer(time, thick) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                playerFailed();
            }
        }.start();
    }

    public void stopTimers() {
        if (blockShowTimer != null) blockShowTimer.cancel();
        if (newBlockTimer != null) newBlockTimer.cancel();
        if (newBlockTimer != null) nextGameTimer.cancel();
        if (playerWaitTimer != null) playerWaitTimer.cancel();
    }
}
