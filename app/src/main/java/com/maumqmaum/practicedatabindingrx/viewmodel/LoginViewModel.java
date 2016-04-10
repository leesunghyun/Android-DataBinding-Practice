package com.maumqmaum.practicedatabindingrx.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.maumqmaum.practicedatabindingrx.BR;
import com.maumqmaum.practicedatabindingrx.model.LoginModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class LoginViewModel extends BaseObservable {

    private LoginModel loginModel;

    public LoginViewModel() {
        loginModel = new LoginModel();
    }

    public String getId() {
        return loginModel.getId();
    }

    public String getPassword() {
        return loginModel.getPassword();
    }

    @Bindable
    public String getIdError() {
        if (loginModel.getId() == null || "dave".equals(loginModel.getId())) {
            return null;
        } else {
            return "hint : dave";
        }
    }

    @Bindable
    public String getPasswordError() {
        if (loginModel.getPassword() == null || "1234".equals(loginModel.getPassword())) {
            return null;
        } else {
            return "hint : 1234";
        }
    }

    @Bindable
    public boolean isEnabled() {
        return !TextUtils.isEmpty(loginModel.getId()) && !TextUtils.isEmpty(loginModel.getPassword());
    }

    public void bindId(Observable<CharSequence> observable) {
        observable.throttleLast(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        loginModel.setId(charSequence.toString());
                        notifyPropertyChanged(BR.enabled);
                    }
                });
    }

    public void bindPassword(Observable<CharSequence> observable) {
        observable.throttleLast(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        loginModel.setPassword(charSequence.toString());
                        notifyPropertyChanged(BR.enabled);
                    }
                });
    }
}
