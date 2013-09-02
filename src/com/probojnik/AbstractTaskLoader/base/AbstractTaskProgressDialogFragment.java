package com.probojnik.AbstractTaskLoader.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * @author Stanislav Shamji
 */
public abstract class AbstractTaskProgressDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Object>, DialogInterface.OnCancelListener {
    private static final String TAG = "AbstractTaskProgressDialogFragment";

    private ProgressDialog progressDialog;

    private final AbstractTaskLoader loader;
    protected abstract void onLoadComplete(Object data);
    protected abstract void onCancelLoad();

    protected AbstractTaskProgressDialogFragment(AbstractTaskLoader loader,String message){
        loader.setHandler(handler);
        this.loader = loader;

        Bundle args = new Bundle();
        args.putString("message", message);
        setArguments(args);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this is really important in order to save the state across screen
        // configuration changes for example
        setRetainInstance(true);

        // ---- magic lines starting here -----
        // call this to re-connect with an existing
        // loader (after screen configuration changes for e.g!)
        LoaderManager lm = getLoaderManager();
        if (lm.getLoader(0) != null) {

            lm.initLoader(0, loader.getArguments(), this);
        }else{
            //new auto start asynctask loader
            startLoading();
        }
        // ----- end magic lines -----
    }

    @Override
    public ProgressDialog onCreateDialog(Bundle savedInstanceState) {

        String message = getArguments().getString("message");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setOnCancelListener(this);
        progressDialog.show();

        return progressDialog;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);

        super.onDestroyView();
    }

    protected void startLoading() {
        // first time we call this loader, so we need to create a new one
        getLoaderManager().initLoader(0, loader.getArguments(), this);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        onLoadComplete(data);
        ((AbstractTaskLoader) loader).setHandler(null);

        hideDialog();
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        loader.cancelLoad(); 		//base metod
        loader.setCanseled(true);	//custom flag

        onCancelLoad();
        //do not invoke dismiss for this dialog
    }

    private void hideDialog() {

        handler.post(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        });
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == AbstractTaskLoader.MSGCODE_MESSAGE){
                String message = AbstractTaskLoader.getMessageValue(msg);
                if(message!=null){
                    //update progress bar message
                    if(progressDialog!=null){
                        progressDialog.setMessage(message);
                    }
                }
            }
        }
    };
}
