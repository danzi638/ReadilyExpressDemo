package com.example.jiayin.readilyexpressdemo.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.community.activity.CallActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.SendMessageActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.ShoppingActivity;
import com.example.jiayin.readilyexpressdemo.community.bean.CommunityItem;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jiayin on 2017/6/30.
 */

public class CommunityFragmentAdapter extends RecyclerView.Adapter {
    /**
     * 广告横幅类型
     */
    public static final int ADVERTISING = 0;
    /**
     * 频道
     */
    public static final int CHANNEL = 1;


    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private int currentType = ADVERTISING;

    public CommunityFragmentAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADVERTISING) {
            return new AdvertisingViewHolder(mContext, mLayoutInflater.inflate(R.layout.advertising_viewpager, null));
        } else if (viewType == CHANNEL) {
            return new ChannelViewHolder(mContext, mLayoutInflater.inflate(R.layout.channel_item, null));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ADVERTISING) {
            AdvertisingViewHolder advertisingViewHolder = (AdvertisingViewHolder) holder;
            advertisingViewHolder.setData();
        } else if (getItemViewType(position) == CHANNEL) {
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            channelViewHolder.setData();
        }
    }

    private List<CommunityItem> communityItemArrayList = new ArrayList<CommunityItem>();

    /**
     * 频道页面
     */
    class ChannelViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        private ListView lv_channel;
        private ChannelAdapter adapter;

        public ChannelViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            lv_channel = (ListView) itemView.findViewById(R.id.lv_channel);

        }

        public void setData() {
            initCommunity();
            ChannelAdapter adapter = new ChannelAdapter(mContext, R.layout.item_channel, communityItemArrayList);
            lv_channel.setAdapter(adapter);

            lv_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                private Intent intent;

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (i) {
                        case 0:
                            //朋友圈
                            intent = new Intent(mContext, SendMessageActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:

                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            intent = new Intent(mContext, CallActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            intent = new Intent(mContext, ShoppingActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                    }
//                    Toast.makeText(mContext,"get：" + i + ":" + communityItemArrayList.get(i).getName(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initCommunity() {
            CommunityItem apple = new CommunityItem("朋友圈", R.drawable.community_contatct_group);
            communityItemArrayList.add(apple);
            CommunityItem banana = new CommunityItem("求助", R.drawable.community_help);
            communityItemArrayList.add(banana);
            CommunityItem orange = new CommunityItem("帮忙", R.drawable.community_help_for);
            communityItemArrayList.add(orange);
            CommunityItem watermelon = new CommunityItem("打电话", R.drawable.community_call);
            communityItemArrayList.add(watermelon);
            CommunityItem pear = new CommunityItem("看点", R.drawable.community_news);
            communityItemArrayList.add(pear);
            CommunityItem grape = new CommunityItem("购物", R.drawable.community_shopping);
            communityItemArrayList.add(grape);
        }
    }

    /**
     * 广告横幅
     */
    class AdvertisingViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        private Banner banner;

        public AdvertisingViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            this.banner = (Banner) itemView.findViewById(R.id.banner);
        }

        public void setData() {
            final List<Integer> imagesUrl = new ArrayList<>();
            imagesUrl.add(R.drawable.advertising_1);
            imagesUrl.add(R.drawable.advertising_2);
            imagesUrl.add(R.drawable.advertising_3);
            //设置循环指示点
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置手风琴效果
            banner.setBannerAnimation(Transformer.Accordion);

            banner.setImages(imagesUrl);

            //设置item的点击事件
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case ADVERTISING:
                currentType = ADVERTISING;
                break;
            case CHANNEL:
                currentType = CHANNEL;
                break;
        }
        return currentType;
    }

    /**
     * 总共有多少个item
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
