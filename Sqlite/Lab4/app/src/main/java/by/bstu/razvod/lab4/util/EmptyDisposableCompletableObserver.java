package by.bstu.razvod.lab4.util;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class EmptyDisposableCompletableObserver extends AutoDisposableCompletableObserver {
    public EmptyDisposableCompletableObserver() {
        super();
    }


    public EmptyDisposableCompletableObserver(CompositeDisposable lifecycleBag) {
        super(lifecycleBag);
    }
}
