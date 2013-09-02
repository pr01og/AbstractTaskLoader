package com.probojnik.AbstractTaskLoader;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.probojnik.AbstractTaskLoader.base.ITaskLoaderListener;

/**
 * @author Stanislav Shamji
 */
public class SampleActivity extends FragmentActivity implements ITaskLoaderListener {
    private static final String TAG = "SampleActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button bnt = (Button) findViewById(R.id.button1);
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowProgress();
            }
        });

    }

    protected void doShowProgress() {
        SampleTask.execute(this, this);
    }

    @Override
    public void onCancelLoad() {
        Log.d(TAG, "task canceled");
    }

    @Override
    public void onLoadFinished(Object data) {
        if(data!=null && data instanceof String){
            Log.d(TAG, "task result: " + data);
        }
    }
}