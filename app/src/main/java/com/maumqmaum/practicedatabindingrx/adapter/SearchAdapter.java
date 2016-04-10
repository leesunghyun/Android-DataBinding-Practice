package com.maumqmaum.practicedatabindingrx.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.maumqmaum.practicedatabindingrx.R;
import com.maumqmaum.practicedatabindingrx.databinding.ListSearchItemBinding;
import com.maumqmaum.practicedatabindingrx.model.TweetModel;

public class SearchAdapter extends ArrayAdapter<TweetModel> {

    public SearchAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ListSearchItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()), R.layout.list_search_item, parent, false);
            convertView = binding.getRoot();
        }
        ListSearchItemBinding binding = DataBindingUtil.getBinding(convertView);
        binding.setTweet(getItem(position));
        return convertView;
    }
}
