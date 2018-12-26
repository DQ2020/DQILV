package com.open.ilv;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ImpressionsLabelView extends ViewGroup {

    private int itemMargin = 10;
    private boolean isMoveAble = false;
    private boolean isClickAble = false;
    private boolean isLongClickAble = false;
    private OnClickListener clickListener = null;
    private OnLongClickListener longClickListener = null;

    public ImpressionsLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int meaWidth = MeasureSpec.getSize(widthMeasureSpec);
        int meaHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int tempWidth = 0;
        int tempHeight = 0;
        int currentLineWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childWidth > meaWidth) {
                throw new RuntimeException("Label width bigger than outside view");
            }
            if (currentLineWidth + childWidth > meaWidth) {
                //换行
                currentLineWidth = childWidth;
                tempHeight += childHeight;
            } else {
                tempWidth += childWidth;
                currentLineWidth += childWidth;
            }
            tempWidth = Math.max(tempWidth, currentLineWidth);
        }
        Log.d("2020", widthSpec + "");
        Log.d("2020", heightSpec + "");
        switch (widthSpec) {
            case MeasureSpec.AT_MOST:
                tempWidth = Math.min(tempWidth, meaWidth);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                tempWidth = meaWidth;
                break;
        }
        switch (heightSpec) {
            case MeasureSpec.AT_MOST:
                tempHeight = Math.min(tempHeight, meaHeight);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                tempHeight = meaHeight;
                break;
        }
        setMeasuredDimension(tempWidth, tempHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currentLineHeight = itemMargin;
        int currentLineWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (currentLineWidth + childWidth + itemMargin * 2 <= r) {
                currentLineWidth += childWidth + itemMargin * 2;
            } else {
                //换行
                currentLineWidth = childWidth + itemMargin * 2;
                currentLineHeight += childHeight + itemMargin * 2;
            }
            child.layout(currentLineWidth - childWidth - itemMargin, currentLineHeight, currentLineWidth - itemMargin, currentLineHeight + childHeight);
        }
    }

    public void addLabel(String label) {
        RoundConnerTextView labelView = new RoundConnerTextView(getContext());
        labelView.setText(label);
        labelView.setTextColor(Color.WHITE);
        labelView.setPadding(10, 10, 10, 10);
        labelView.setBackgroundColor(Color.rgb(
                (int) (Math.random() * 240),
                (int) (Math.random() * 240),
                (int) (Math.random() * 240)));
        addView(labelView);

        if (isClickAble) {
            if (clickListener != null)
                labelView.setOnClickListener(clickListener);
            else
                labelView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), ((RoundConnerTextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        }

        if (isLongClickAble) {
            if (longClickListener != null)
                labelView.setOnLongClickListener(longClickListener);
            else
                labelView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (v.isLongClickable() && isMoveAble)
                            v.layout((int) -v.getX(), (int) -v.getY(), 0, 0);
                        return true;
                    }
                });
        }

        if (isMoveAble) {
            labelView.setOnTouchListener(new OnTouchListener() {
                float rx = 0;
                float ry = 0;
                int measuredWidth = 0;
                int measuredHeight = 0;
                float vx = 0;
                float vy = 0;
                //解决点击位移 手指移动出item范围开始跟随手指移动
                //识别为开始滑动就持续滑动 解决跳跃滑动视觉
                //即手指未移出item也跟随手指滑动
                //开始移动关闭长按删除功能
                boolean isMove;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getX() < 0) {
                        return false;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            measuredWidth = v.getMeasuredWidth();
                            measuredHeight = v.getMeasuredHeight();
                            Log.d("2020", "measuredWidth:" + measuredWidth);
                            Log.d("2020", "measuredHeight:" + measuredHeight);
                            v.performClick();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            rx = event.getRawX();
                            ry = event.getRawY();
                            vx = v.getX();
                            vy = v.getY();
                            Log.d("2020", "rx:" + rx);
                            Log.d("2020", "ry:" + ry);
                            Log.d("2020", "vx:" + vx);
                            Log.d("2020", "vy:" + vy);
                            if (isMove
                                    || rx > vx + measuredWidth
                                    || rx < vx
                                    || ry > vy + measuredHeight + getStatusBarHeight()
                                    || ry < vy + getStatusBarHeight()) {
                                v.layout((int) (event.getRawX() - measuredWidth / 2),
                                        (int) (event.getRawY() - measuredHeight / 2 - getStatusBarHeight()),
                                        (int) event.getRawX() + measuredWidth / 2,
                                        (int) event.getRawY() + measuredHeight / 2 - getStatusBarHeight());
                                isMove = true;
                            }
                            if (isMove) {
                                v.setLongClickable(false);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            isMove = false;
                            v.setLongClickable(true);
                            break;
                    }
                    return ImpressionsLabelView.super.onTouchEvent(event);
                }
            });
        }
    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public void setItemMargin(int margin) {
        this.itemMargin = margin;
    }

    public void setMoveAble(boolean moveAble) {
        isMoveAble = moveAble;
    }

    public void setClickAble(boolean clickAble) {
        isClickAble = clickAble;
    }

    public void setLongClickAble(boolean longClickAble) {
        isLongClickAble = longClickAble;
    }

    public void setClickListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    public void setLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }
}
