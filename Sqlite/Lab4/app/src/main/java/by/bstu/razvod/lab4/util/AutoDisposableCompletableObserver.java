package by.bstu.razvod.lab4.util;

import android.util.Log;

import androidx.annotation.CallSuper;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;

public abstract class AutoDisposableCompletableObserver extends DisposableCompletableObserver {

    private static String TAG = "TAG";

    public AutoDisposableCompletableObserver() {

    }

    public AutoDisposableCompletableObserver(CompositeDisposable lifecycleBag) {
        lifecycleBag.add(this);
    }

    @Override
    @CallSuper
    public void onComplete() {
        dispose();
    }

    @CallSuper
    @Override
    public void onError(@NonNull Throwable e) {
        Log.d(TAG, e.toString());
        dispose();
    }
}
