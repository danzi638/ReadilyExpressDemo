package com.example.jiayin.readilyexpressdemo.community.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.community.adapter.CommunityFragmentAdapter;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * Created by jiayin on 2017/6/27.
 */

public class CommunityFragment extends Fragment {


    private RecyclerView rvHome;
    private CommunityFragmentAdapter adapter;

    public void initView(View view) {
        rvHome = (RecyclerView) view.findViewById(R.id.rv_home);
        initListener();
    }
    private void initListener() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "主页视图被初始化了");

        View view = View.inflate(getActivity(), R.layout.fragment_community,null);
        initView(view);
        return view;
    }


    /**
     * 当Activity被创建的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 当子类需要联网请求数据的时候，可以重写该方法,在该方法中联网
     */
    public void initData(){
        Log.e(TAG, "这是一个社区页面");
        processData();

    }

    private void processData() {

        adapter = new CommunityFragmentAdapter(getActivity());
        rvHome.setAdapter(adapter);

        rvHome.setLayoutManager(new GridLayoutManager(getActivity(),1));
        Log.e(TAG, "解析成功: ");

    }
}
