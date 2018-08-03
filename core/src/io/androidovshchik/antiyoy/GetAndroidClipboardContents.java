package io.androidovshchik.antiyoy;

import android.os.Handler;
import android.os.Looper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import java.util.TimerTask;

public class GetAndroidClipboardContents extends TimerTask {
    boolean complete = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    String result = "None";

    class C00421 implements Runnable {
        C00421() {
        }

        public void run() {
            Clipboard clipboard = Gdx.app.getClipboard();
            GetAndroidClipboardContents.this.result = clipboard.getContents();
            GetAndroidClipboardContents.this.complete = true;
            System.out.println("---------------------- 1");
        }
    }

    public void run() {
        this.handler.post(new C00421());
    }

    public boolean isComplete() {
        return this.complete;
    }

    public String getResult() {
        return this.result;
    }
}
