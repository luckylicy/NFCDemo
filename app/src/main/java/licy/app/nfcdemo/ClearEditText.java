package licy.app.nfcdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * ClearEditText
 * description: 带删除按钮的输入框
 *
 * @author : Licy
 * @date : 2021/3/17
 * email ：licy3051@qq.com
 */
public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {

    private Drawable mClearDrawable;

    public ClearEditText(@NonNull Context context) {
        super(context);
        init();
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 获取用户自定义的清除drawable
        Drawable[] compoundDrawables = getCompoundDrawables();
        mClearDrawable = compoundDrawables[2];
        // 如果没有设置，则使用默认的
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.icon_edit_text_delete);
        }
        // 设置清除按钮的边界
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setCompoundDrawablePadding(12);
        setPadding(getPaddingLeft(), getPaddingTop(), 12, getPaddingBottom());
        // 默认隐藏清除按钮
        hideClearDrawable();

        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private void showClearDrawable() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                mClearDrawable, getCompoundDrawables()[3]);
    }

    private void hideClearDrawable() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                null, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            showClearDrawable();
        } else {
            hideClearDrawable();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Editable text = getText();
            if (text != null && text.length() > 0) {
                showClearDrawable();
            } else {
                hideClearDrawable();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 手抬起的时候，判断是否触摸的是删除按钮
                boolean xTouchable =
                        (event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth())) && (event.getX() < (getWidth() - getPaddingRight()));
                boolean yTouchable =
                        (event.getY() > ((getHeight() - mClearDrawable.getIntrinsicHeight()) / 2)) && (event.getY() < ((getHeight() + mClearDrawable.getIntrinsicHeight()) / 2));
                //清除文本
                if (xTouchable && yTouchable) {
                    setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
