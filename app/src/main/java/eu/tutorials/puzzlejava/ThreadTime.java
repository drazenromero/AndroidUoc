package eu.tutorials.puzzlejava;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ThreadTime extends Thread{

    private ThreadTimeObjectSync threadTimeObjectSync;
    private Activity parentActivity;
    public ThreadTime(ThreadTimeObjectSync threadTimeObjectSync, Activity parentActivity){
        this.threadTimeObjectSync = threadTimeObjectSync;
        this.parentActivity = parentActivity;
    }

    @Override
    public void run(){
        System.out.println("Se ha iniciado el thread de time");
        try {
            while(true) {
                Thread.sleep(1000);
                synchronized (threadTimeObjectSync){
                    if(threadTimeObjectSync.getContinueTime() == true) {

                        threadTimeObjectSync.setSeconds(threadTimeObjectSync.getSeconds() + 1);
                        //System.out.println(String.format("Has passed %d",threadTimeObjectSync.getSeconds()));
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView txt = parentActivity.findViewById(R.id.TimeCounter);
                                txt.setText(String.format("Time: %dS", threadTimeObjectSync.getSeconds()));
                            }
                        });
                    }else{

                    }



                }



            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
