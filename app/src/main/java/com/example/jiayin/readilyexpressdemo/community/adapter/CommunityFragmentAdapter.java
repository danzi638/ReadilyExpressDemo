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
import com.example.jiayin.readilyexpressdemo.community.activity.HelpActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.HelpForActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.HotNewActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.SendMessageActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.ShareActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.ShoppingActivity;
import com.example.jiayin.readilyexpressdemo.community.activity.WeatherAcitivty;
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
        private GridView gv_channel;
        private ChannelAdapter adapter;

        public ChannelViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            gv_channel = (GridView) itemView.findViewById(R.id.gv_channel);

        }

        public void setData() {
            initCommunity();
            ChannelAdapter adapter = new ChannelAdapter(mContext, R.layout.item_channel, communityItemArrayList);
            gv_channel.setAdapter(adapter);

            gv_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                private Intent intent;

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    switch (i) {
                        case 0:
                            //朋友圈
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //求助
                            intent = new Intent(mContext, HelpActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //帮忙
                            intent = new Intent(mContext, HelpForActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            //打电话
                            intent = new Intent(mContext, CallActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            //天气
                            intent = new Intent(mContext, WeatherAcitivty.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 5:
                            //发短信
                            intent = new Intent(mContext, SendMessageActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 6:
                            //购物
                            intent = new Intent(mContext, ShoppingActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 7:
                            //热点
                            intent = new Intent(mContext, HotNewActivity.class);
                            mContext.startActivity(intent);
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case 8:
                            //更多
                            Toast.makeText(mContext, "get：" + i + ":" + communityItemArrayList.get(i).getName() + "暂时没有更多", Toast.LENGTH_SHORT).show();
                            break;
                    }
//                    Toast.makeText(mContext,"get：" + i + ":" + communityItemArrayList.get(i).getName(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initCommunity() {
            CommunityItem friend = new CommunityItem("朋友圈", R.drawable.community_friend);
            communityItemArrayList.add(friend);
            CommunityItem help = new CommunityItem("求助", R.drawable.community_help);
            communityItemArrayList.add(help);
            CommunityItem helpfor = new CommunityItem("帮忙", R.drawable.community_help_for);
            communityItemArrayList.add(helpfor);
            CommunityItem phone = new CommunityItem("打电话", R.drawable.community_call);
            communityItemArrayList.add(phone);
            CommunityItem message = new CommunityItem("天气", R.drawable.community_weather);
            communityItemArrayList.add(message);
            CommunityItem shipin = new CommunityItem("发短信", R.drawable.community_message);
            communityItemArrayList.add(shipin);
            CommunityItem shopping = new CommunityItem("购物", R.drawable.community_shopping);
            communityItemArrayList.add(shopping);
            CommunityItem hot = new CommunityItem("热点", R.drawable.community_hot);
            communityItemArrayList.add(hot);
            CommunityItem more = new CommunityItem("更多", R.drawable.community_more);
            communityItemArrayList.add(more);
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
