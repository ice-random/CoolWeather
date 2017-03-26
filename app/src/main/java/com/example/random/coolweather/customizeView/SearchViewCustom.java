package com.example.random.coolweather.customizeView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.random.coolweather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by random on 17/3/24.
 */

public class SearchViewCustom extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.et_input)
    EditText input;

    @BindView(R.id.btn_delete)
    ImageButton delete;

    @BindView(R.id.btn_back)
    ImageButton back;

//    private Button search;

    public SearchViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_view, this, true);
        ButterKnife.bind(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                onClickDelete();
                break;
            case R.id.btn_back:
                onClickBack();
                break;
            case R.id.btn_search:
                onClickSearch();
                break;
        }
    }


    /**
     * 点击了搜索按钮
     */
    private void onClickSearch() {

    }

    /**
     * 点击了返回按钮
     */
    private void onClickBack() {

    }

    /**
     * 点击了删除按钮
     */
    private void onClickDelete() {
    }

    public interface SearchViewCustomListener{
        void BackListener();
    }
}

