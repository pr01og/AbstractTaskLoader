package com.probojnik.AbstractTaskLoader.base;

/**
 * @author Stanislav Shamji
 */
public interface ITaskLoaderListener {
    void onLoadFinished(Object data);
    void onCancelLoad();
}
