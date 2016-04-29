package menu.catz.aaron.catzmain;

/**
 * Created by Zacc on 2016-04-28.
 */

public class PseudoTimer {
    private long[] alFrameCount;
    private long lInternalTime;
    private long timeBank = 0;

    //Initializes the timer
    PseudoTimer() {
        lInternalTime = System.currentTimeMillis();
        alFrameCount = new long[60];
        for (int i = 0; i < alFrameCount.length; i++) {
            alFrameCount[i] = 0;
        }
    }

    //Updates the timer
    public void Update() {
        long passedT = getPassedTime();
        timeBank += passedT;
    }

    //Gets the amount of time that has passed since this function has been called
    private long getPassedTime() {
        long passedTime = System.currentTimeMillis() - lInternalTime;
        lInternalTime = System.currentTimeMillis();
        return passedTime;
    }

    //Use this to determine when to run your game code to reach your desired FPS
    //Calculates for desired FPS
    public boolean frameReady(float lFramerate) {
        float fMillisecondsPerFrame = 1000 / lFramerate;
        if (timeBank > fMillisecondsPerFrame) {
            for (int i = 0; i < (alFrameCount.length - 1); i++) {
                alFrameCount[i] = alFrameCount[i + 1];
            }
            alFrameCount[59] = timeBank;
            timeBank = 0;
            return true;
        }
        return false;
    }

    //Calculates how long it has taken to generate the last 60 frames, and delivers it in FPS
    public float getActualFPS() {
        float lTotalTimeForSavedInstances = 0;
        for (int i = 0; i < alFrameCount.length; i++){
            lTotalTimeForSavedInstances += alFrameCount[i];
        }
        return (alFrameCount.length / (lTotalTimeForSavedInstances/1000));
    }

    public class UIThreadCommand {
        public void runCommand() {
        }

        public void start(MainActivity main) {
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runCommand();
                }
            });
        }
    }
}