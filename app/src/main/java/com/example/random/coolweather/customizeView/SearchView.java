package com.example.random.coolweather.customizeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by random on 17/3/10.
 */

public class SearchView extends android.support.v7.widget.SearchView{

    Paint mPaint;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
