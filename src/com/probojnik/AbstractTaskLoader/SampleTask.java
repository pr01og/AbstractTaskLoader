package com.probojnik.AbstractTaskLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.probojnik.AbstractTaskLoader.base.AbstractTaskLoader;
import com.probojnik.AbstractTaskLoader.base.ITaskLoaderListener;
import com.probojnik.AbstractTaskLoader.base.TaskProgressDialogFragment;

/**
 * @author Stanislav Shamji
 */
public class SampleTask extends AbstractTaskLoader {
    private static final String TAG = "SampleTask";

    public static void execute(FragmentActivity fa, ITaskLoaderListener taskLoaderListener) {

        SampleTask loader = new SampleTask(fa);

        new TaskProgressDialogFragment.Builder(fa, loader, "Wait...").setCancelable(true).setTaskLoaderListener(taskLoaderListener).show();
    }

    protected SampleTask(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {

        String result = "";
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = "Wait: " + String.valueOf(i);
            publishMessage(result);

            if (isCanselled()) {
                break;
            }

            Log.d(TAG, result);
        }
        return result;
    }

    @Override
    public Bundle getArguments() {
        return null;
    }

    @Override
    public void setArguments(Bundle args) {

    }

}
