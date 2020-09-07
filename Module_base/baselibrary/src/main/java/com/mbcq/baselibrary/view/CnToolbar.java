package com.mbcq.baselibrary.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mbcq.baselibrary.R;

/**
 * 自定义toolbar样式
 */

public class CnToolbar extends Toolbar {


    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private ImageView right_iv, back_iv;
    private ConstraintLayout father_cl;


    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public CnToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        initView();
        //设置toolbar的边距
        setContentInsetsRelative(10, 10);

        if (attrs != null) {
            @SuppressLint("RestrictedApi") final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            /**
             * 自定义控件的模式：1.通过TintTypedArray类从attrs.xml中取得自定义属性值
             * 2.如果属性值非空，将其赋值
             */
            @SuppressLint("RestrictedApi") final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            //一定要在这里进行条件判断
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }
            Boolean isShowBackIcon = a.getBoolean(R.styleable.CnToolbar_backButtonIsShow, false);
            setIsShowBackIcon(isShowBackIcon);
            int toolBarBackGroundColor = a.getColor(R.styleable.CnToolbar_toolbarBackGround, 0);
            setToolbarBackGround(toolBarBackGroundColor);



          /*  //默认false
            @SuppressLint("RestrictedApi")
            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);
            //如果isShowSearchView为true，把Title隐藏
            if (isShowSearchView) {

                showSearchView();
                hideTitleView();

            }
*/
            @SuppressLint("RestrictedApi")
            CharSequence centerTextViewText = a.getText(R.styleable.CnToolbar_centerTextViewText);
            if (centerTextViewText != null) {
                setCenterTitleText(centerTextViewText);
            }

            a.recycle();
        }

    }

    private void initView() {

        if (mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.custom_toolbar, null);

            father_cl = findViewById(R.id.father_cl);
            mTextTitle = (TextView) mView.findViewById(R.id.center_tv);
            right_iv = (ImageView) mView.findViewById(R.id.right_iv);
            back_iv = (ImageView) mView.findViewById(R.id.back_iv);

            //把Toolbar里面的控件组合起来
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }

    }

    //对右边的Button进行Background设置
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon) {

        if (right_iv != null) {

            right_iv.setImageDrawable(icon);
            right_iv.setVisibility(VISIBLE);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setIsShowBackIcon(Boolean isShowBackIcon) {

        if (back_iv != null) {

            back_iv.setVisibility(isShowBackIcon ? VISIBLE : GONE);
        }

    }

    public void setToolbarBackGround(int colorId) {
        if (father_cl != null) {
            if (colorId != 0)
                father_cl.setBackgroundResource(colorId);
        }

    }

    public void setRightButtonIcon(int icon) {

        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonOnClickListener(OnClickListener li) {

        right_iv.setOnClickListener(li);
    }

    public void setBackButtonOnClickListener(OnClickListener li) {

        back_iv.setOnClickListener(li);
    }

    public void setCenterTitleText(CharSequence text) {
        mTextTitle.setText(text);
        mTextTitle.setVisibility(VISIBLE);
    }


    public void setCenterTitleText(int id) {
        setCenterTitleText(getResources().getString(id));
    }


    //设置标题
    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }

    }


    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


}