package com.example.jiayin.readilyexpressdemo.community.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.community.bean.CommunityItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jiayin on 2017/6/30.
 */

/**
 * 频道适配器
 */
class ChannelAdapter extends ArrayAdapter<CommunityItem> {

    private int resourceId;

    public ChannelAdapter(Context context, int textViewResourceId,
                        List<CommunityItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommunityItem communityItem = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = (ImageView) view.findViewById (R.id.community_image);
            viewHolder.fruitName = (TextView) view.findViewById (R.id.community_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.fruitImage.setImageResource(communityItem.getImageId());
        viewHolder.fruitName.setText(communityItem.getName());
        return view;
    }

    class ViewHolder {

        ImageView fruitImage;
        TextView fruitName;

    }
}
