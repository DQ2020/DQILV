package com.open.ilv;

import android.content.Context;

public class RoundConnerTextView extends android.support.v7.widget.AppCompatTextView {

    public RoundConnerTextView(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundColor(int color) {
        setBackground(new RoundConnerDrawable(color));
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
