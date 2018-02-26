/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.List;

public class TextAnimator {
    public TextAnimator(boolean animateFast, final Activity activity, final int scrollViewId, final int layoutId, final List<String> textArray){
        this(animateFast, activity, scrollViewId, layoutId, textArray, null);
    }

    public TextAnimator(final boolean animateFast, final Activity activity, final int scrollViewId, final int layoutId, final List<String> textArray, final AnimationListDone doneListener){
        new Thread(){
            public void run(){
                for(String text : textArray){
                    animate(animateFast, activity, text, scrollViewId, layoutId);
                }
                if(doneListener!=null){
                    doneListener.done();
                }
            }
        }.start();
    }

    private FadeyTextView animate(boolean animateFast, final Activity activity, final String text, final int scrollViewId, final int layoutId){
        final FadeyTextView textView = new FadeyTextView(activity);
        textView.setDurationPerLetter(animateFast?30:150);
        activity.runOnUiThread(
            new Runnable() {
                public void run() {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f);
                    textView.setTextColor(Color.GREEN);
                    textView.setTypeface(Typeface.MONOSPACE);
                    textView.setFocusable(true);
                    textView.setFocusableInTouchMode(true);
                    int padding = (int)(16 * activity.getResources().getDisplayMetrics().density);
                    textView.setPadding(padding, padding, padding, padding);
                    final ScrollView scrollView = ((ScrollView)activity.findViewById(scrollViewId));
                    ViewGroup group = ((ViewGroup)activity.findViewById(layoutId));
                    textView.setText(text);
                    scrollView.post(new Runnable() {
                        public void run() {
                            //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            scrollView.scrollTo(0, scrollView.getBottom());
                            textView.requestFocus();
                        }
                    });
                    group.addView(textView);
                }
            }
        );
        //if(textView!=null){
            textView.blockIfIsAnimating();
        //}
        return textView;
    }
}
