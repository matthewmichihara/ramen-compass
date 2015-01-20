package com.fourpool.ramencompass;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by mattm on 9/19/14.
 */
public class VenueView extends LinearLayout {
    public VenueView(Context context) {
        this(context, null, 0);
    }

    public VenueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VenueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
