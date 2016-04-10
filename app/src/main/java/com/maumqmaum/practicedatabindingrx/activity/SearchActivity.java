package com.maumqmaum.practicedatabindingrx.activity;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.maumqmaum.practicedatabindingrx.R;
import com.maumqmaum.practicedatabindingrx.adapter.SearchAdapter;
import com.maumqmaum.practicedatabindingrx.databinding.SearchBinding;
import com.maumqmaum.practicedatabindingrx.model.TweetModel;
import com.maumqmaum.practicedatabindingrx.network.repository.TwitterRepository;
import com.maumqmaum.practicedatabindingrx.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import timber.log.Timber;
import twitter4j.QueryResult;
import twitter4j.Status;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SearchBinding binding;

    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchViewModel();
        binding.setViewModel(viewModel);
        binding.listView.setOnItemClickListener(this);
        viewModel.bindSearch(RxTextView.textChanges(binding.searchText));
    }

    public void onSearchBtnClick(View view) {
        TwitterRepository.getInstance()
                .requestSearch(viewModel.getSearchText())
                .subscribe(new Action1<QueryResult>() {
                    @Override
                    public void call(QueryResult result) {
                        List<TweetModel> list = new ArrayList<>();
                        for (Status status : result.getTweets()) {
                            TweetModel tweetModel = new TweetModel();
                            tweetModel.setText(status.getText());
                            list.add(tweetModel);
                        }
                        binding.setTweets(list);
                    }
                });
    }

    @BindingAdapter("items")
    public static void setTweets(ListView listView, List<TweetModel> tweetModels) {
        Timber.d("BindingAdapter setTweets");
        SearchAdapter adapter = new SearchAdapter(listView.getContext());
        if (tweetModels != null) {
            adapter.addAll(tweetModels);
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Timber.d("OnItemClickListener positon > %s", binding.getTweets().get(position).getText());
    }
}
