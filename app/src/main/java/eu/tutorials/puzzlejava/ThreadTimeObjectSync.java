package eu.tutorials.puzzlejava;

public class ThreadTimeObjectSync {
    private int seconds = 0;
    private boolean continueTime = true;
    public void setSeconds(int sec){
        seconds = sec;
    }
    public int getSeconds(){
        return  seconds;
    }
    public void setContinueTime(boolean con){continueTime = con;}
    public boolean getContinueTime(){return continueTime;}
}
