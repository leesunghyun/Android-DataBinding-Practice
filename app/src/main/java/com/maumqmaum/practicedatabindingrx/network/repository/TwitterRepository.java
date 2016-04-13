package com.maumqmaum.practicedatabindingrx.network.repository;

import com.maumqmaum.practicedatabindingrx.App;
import com.maumqmaum.practicedatabindingrx.R;
import com.maumqmaum.practicedatabindingrx.util.PreferenceUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterRepository {

    private static TwitterRepository instance;

    private Twitter twitter;

    @Getter
    @Setter
    private RequestToken requestToken;

    private TwitterRepository() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(App.getInstance().getString(R.string.twitter_consumer_key));
        configurationBuilder.setOAuthConsumerSecret(App.getInstance().getString(R.string.twitter_secret_key));
        Configuration configuration = configurationBuilder.build();
        twitter = new TwitterFactory(configuration).getInstance();
        AccessToken accessToken = App.getPreferenceUtil()
                .getObejct(PreferenceUtil.KEY_TWITTER_ACCESS_TOKEN, AccessToken.class);
        if (accessToken != null) {
            twitter.setOAuthAccessToken(accessToken);
        }
    }

    public static synchronized TwitterRepository getInstance() {
        if (instance == null) {
            instance = new TwitterRepository();
        }
        return instance;
    }

    public Observable<RequestToken> requestRequestToken() {
        return Observable.defer(new Func0<Observable<RequestToken>>() {
            @Override
            public Observable<RequestToken> call() {
                String callbackUrl = App.getInstance().getString(R.string.twitter_callback_url);
                try {
                    return Observable.just(twitter.getOAuthRequestToken(callbackUrl));
                } catch (Exception e) {
                    Timber.e(e, "Fail to request requestToken");
                }
                return null;
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<AccessToken> requestAccessToken(final String verifier, final RequestToken requestToken) {
        return Observable.defer(new Func0<Observable<AccessToken>>() {
            @Override
            public Observable<AccessToken> call() {
                try {
                    return Observable.just(twitter.getOAuthAccessToken(requestToken, verifier));
                } catch (TwitterException e) {
                    Timber.e(e, "Fail to request AccessToken");
                }
                return null;
            }
        }).subscribeOn(Schedulers.newThread());
    }

    public Observable<QueryResult> requestSearch(final String word) {
        return Observable.defer(new Func0<Observable<QueryResult>>() {
            @Override
            public Observable<QueryResult> call() {
                try {
                    Query query = new Query();
                    query.setQuery(word);
                    return Observable.just(twitter.search(query));
                } catch (TwitterException e) {
                    Timber.e(e, "Fail to search %s", word);
                }
                return null;
            }
        }).subscribeOn(Schedulers.newThread());
    }
}
