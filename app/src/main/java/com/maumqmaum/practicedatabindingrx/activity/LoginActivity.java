package com.maumqmaum.practicedatabindingrx.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.maumqmaum.practicedatabindingrx.App;
import com.maumqmaum.practicedatabindingrx.BR;
import com.maumqmaum.practicedatabindingrx.R;
import com.maumqmaum.practicedatabindingrx.databinding.LoginBinding;
import com.maumqmaum.practicedatabindingrx.network.repository.TwitterRepository;
import com.maumqmaum.practicedatabindingrx.util.PreferenceUtil;
import com.maumqmaum.practicedatabindingrx.viewmodel.LoginViewModel;

import rx.functions.Action1;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class LoginActivity extends AppCompatActivity {

    public LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new LoginViewModel();
        binding.setViewModel(viewModel);
        viewModel.bindId(RxTextView.textChanges(binding.id));
        viewModel.bindPassword(RxTextView.textChanges(binding.password));
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(getString(R.string.twitter_callback_url))) {
            requestTwitterLogin(uri);
        }
    }

    private void requestTwitterLogin(Uri uri) {
        RequestToken requestToken = TwitterRepository.getInstance().getRequestToken();
        if (requestToken == null) {
            TwitterRepository.getInstance().requestRequestToken();
        } else {
            String verifier = uri.getQueryParameter(getString(R.string.twitter_verifier));
            TwitterRepository.getInstance().requestAccessToken(verifier, requestToken)
                    .subscribe(new Action1<AccessToken>() {
                        @Override
                        public void call(AccessToken accessToken) {
                            App.getPreferenceUtil().putObject(PreferenceUtil.KEY_TWITTER_ACCESS_TOKEN, accessToken);
                        }
                    });
        }
    }

    public void onLoginBtnClick(View view) {
        viewModel.notifyPropertyChanged(BR.idError);
        viewModel.notifyPropertyChanged(BR.passwordError);
//        if (viewModel.getIdError() == null && viewModel.getPasswordError() == null) {
        AccessToken accessToken = App.getPreferenceUtil().getObejct(PreferenceUtil.KEY_TWITTER_ACCESS_TOKEN,
                AccessToken.class);
        if (accessToken == null) {
            TwitterRepository.getInstance().requestRequestToken().subscribe(new Action1<RequestToken>() {
                @Override
                public void call(RequestToken requestToken) {
                    TwitterRepository.getInstance().setRequestToken(requestToken);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                }
            });
        } else {
            startActivity(new Intent(this, SearchActivity.class));
        }
        finish();
//        }
    }
}
