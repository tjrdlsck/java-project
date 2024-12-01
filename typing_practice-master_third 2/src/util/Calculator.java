package util;

public class Calculator {
    private long startTime;
    private long totalElapsedTime;
    private int correctChars;
    private int totalInputChars;
    public static int oldTotalInputChars = 0;
    public static int oldCorrectChars = 0;

    public Calculator() {
        resetSession();
    }

    public void resetSession() {
        startTime = System.currentTimeMillis();
        totalElapsedTime = 0;
        correctChars = 0;
        totalInputChars = 0;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void updateElapsedTime() {
        totalElapsedTime = (System.currentTimeMillis() - startTime) / 1000;
    }

    public long getElapsedTime() {
        return totalElapsedTime;
    }

    public void updateOldStats(int tempCorrectChars, int tempTotalInputChars){
        oldCorrectChars += tempCorrectChars;
        oldTotalInputChars += tempTotalInputChars;
    }

    // 실시간 표현을 위한 메서드
    public double getAccuracy() {
        return totalInputChars == 0 ? 0 : (correctChars * 100.0 / totalInputChars);
    }
    // 실시간 표현을 위한 메서드
    public int getTypingSpeed() {
        return totalElapsedTime > 0 ? (int) ((60.0 * correctChars) / totalElapsedTime) : 0;
    }

    public void updateStats(int tempCorrectChars, int tempTotalInputChars) {
        correctChars = tempCorrectChars;
        totalInputChars = tempTotalInputChars;
    }
}




