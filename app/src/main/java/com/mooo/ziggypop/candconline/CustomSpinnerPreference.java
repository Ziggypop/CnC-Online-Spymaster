package com.mooo.ziggypop.candconline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ziggypop on 12/31/15.
 * Credit goes to:
 * http://www.hidroh.com/2015/11/30/building-custom-preferences-v7/
 *
 *
 */
public class CustomSpinnerPreference extends SpinnerPreference{
    public static final String TAG = "TimeIntervalPreference";

    private final LayoutInflater mLayoutInflater;

    public CustomSpinnerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSpinnerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected View createDropDownView(int position, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.spinner_dropdown_item, parent, false);
    }

    @Override
    protected void bindDropDownView(int position, View view) {
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(mEntries[position]);
    }




}
