package com.androidtutz.anushka.rxdemo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="myApp";
    private String greeting="Hello From RxJava";

    private Observable<String> myObservable;
//    private Observer<String> myObserver;
    private DisposableObserver<String> myObserver;
    private DisposableObserver<String> myObserver2;

    // has more than one observer -> Composite Observer
    // use to dispose all the observer at once
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView textView;

//    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tvGreeting);
        myObservable = Observable.just(greeting);

//        // run on IO thread
//        myObservable.subscribeOn(Schedulers.io());
//        // sent back result on main thread
//        myObservable.observeOn(AndroidSchedulers.mainThread());

        // with DisposableObserver, no onSubscribe method
        // contains Observer and Dispose interface
        myObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: observer 1");
                textView.setText(s);
                Toast.makeText(getApplicationContext(), "observer 1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        compositeDisposable.add(myObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(myObserver)
        );
//        myObservable.subscribe(myObserver);

        myObserver2 = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: observer 2");
                textView.setText(s);
                Toast.makeText(getApplicationContext(), "observer 2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        compositeDisposable.add(myObservable
                .subscribeWith(myObserver2)
        );
//        myObservable.subscribe(myObserver2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
