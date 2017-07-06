package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;

import java.util.Arrays;

/**
 * Created by crx on 2017/7/5.
 */

public class EmojiconExampleGroupData {
    private static int[] icons=new int[]{
            R.drawable.e_1,
            R.drawable.e_2,
            R.drawable.e_3,
            R.drawable.e_4,
            R.drawable.e_5,
            R.drawable.e_6,
            R.drawable.e_7,
            R.drawable.e_8,
            R.drawable.e_9,
            R.drawable.e_10,
            R.drawable.e_11,
            R.drawable.e_12,
            R.drawable.e_13,
            R.drawable.e_14,
            R.drawable.e_15,
            R.drawable.e_16,

    };
    private static int[] bigIcons=new int[]{

            R.drawable.e_1,
            R.drawable.e_2,
            R.drawable.e_3,
            R.drawable.e_4,
            R.drawable.e_5,
            R.drawable.e_6,
            R.drawable.e_7,
            R.drawable.e_8,
            R.drawable.e_9,
            R.drawable.e_10,
            R.drawable.e_11,
            R.drawable.e_12,
            R.drawable.e_13,
            R.drawable.e_14,
            R.drawable.e_15,
            R.drawable.e_16,
    };


    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
//            you can replace this to any you want

            datas[i].setIdentityCode("em"+ (1000+i+1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(R.drawable.ee_2);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }


    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }

}
