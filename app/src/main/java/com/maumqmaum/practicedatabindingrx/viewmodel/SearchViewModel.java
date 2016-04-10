package com.maumqmaum.practicedatabindingrx.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.maumqmaum.practicedatabindingrx.BR;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;

public class SearchViewModel extends BaseObservable {

    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    @Bindable
    public boolean isEnabled() {
        return !TextUtils.isEmpty(searchText);
    }

    public void bindSearch(Observable<CharSequence> observable) {
        observable.throttleLast(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        searchText = charSequence.toString();
                        notifyPropertyChanged(BR.enabled);
                    }
                });
    }
}
