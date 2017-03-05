package com.example.random.coolweather; /**
 * Created by random on 17/3/5.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 *
 * 自定义字体图标
 * @author M.Z
 *
 */
public class FontTextView extends TextView {


    /*
     * 控件在xml加载的时候是调用两个参数的构造函数 ，为了自定义的控件的完整性我们可以
     * 都把构造函数写出来
     */
    public FontTextView(Context context) {
        super(context);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }
}
