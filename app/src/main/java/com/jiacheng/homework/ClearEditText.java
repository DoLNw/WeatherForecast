package com.jiacheng.homework;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Context context;
    private Drawable myClearDrawable;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setClearIconVisible(getText().length()>0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //Edit没有单击事件， 所以用触摸的位置来模拟是否点击
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touch = (event.getX()) > (getWidth()-getPaddingRight()-myClearDrawable.getIntrinsicWidth()) &&
                        (event.getX()) < (getWidth()-getPaddingRight());
                if (touch) {
                    setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    private void init() {
        myClearDrawable = getCompoundDrawables()[2];
        if (myClearDrawable == null) {
            myClearDrawable = getResources().getDrawable(R.drawable.clear_delete);
        }
        myClearDrawable.setBounds(0, 0, myClearDrawable.getIntrinsicWidth(), myClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);

        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable drawable = visible ? myClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
    }
}
