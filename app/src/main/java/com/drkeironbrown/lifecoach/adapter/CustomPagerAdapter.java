package com.drkeironbrown.lifecoach.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Image;

import java.io.File;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private OnTouch onTouch;
    private List<Image> list;
    private Context mContext;

    public CustomPagerAdapter(Context context, List<Image> list,OnTouch onTouch) {
        mContext = context;
        this.list = list;
        this.onTouch = onTouch;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = (ViewGroup) inflater.inflate(R.layout.item_slider, collection, false);
        ImageView imgSlider = (ImageView) layout.findViewById(R.id.imgPic);
        Functions.loadImage(mContext, new File(list.get(position).getImagePath()), imgSlider, null);
        imgSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    onTouch.onHold();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    onTouch.onRelease();
                }
                return true;
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    public interface OnTouch{
        void onHold();
        void onRelease();
    }
}