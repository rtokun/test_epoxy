package com.example.artyom.testepoxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CheckableFrameLayout extends FrameLayout implements Checkable {

  private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
  private boolean mChecked = false;
  private OnCheckedChangeListener mOnCheckedChangeListener;

  public CheckableFrameLayout(Context context) {
    super(context);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public boolean isChecked() {
    return mChecked;
  }

  public void setChecked(boolean b) {
    if (b != mChecked) {
      mChecked = b;
      refreshDrawableState();

      if (mOnCheckedChangeListener != null) {
        mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
      }
    }
  }

  public void toggle() {
    setChecked(!mChecked);
  }

  @Override
  public int[] onCreateDrawableState(int extraSpace) {
    final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE_SET);
    }
    return drawableState;
  }

  /**
   * Register a callback to be invoked when the checked state of this view changes.
   *
   * @param listener the callback to call on checked state change
   */
  public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
    mOnCheckedChangeListener = listener;
  }

  /**
   * Interface definition for a callback to be invoked when the checked state of this View is
   * changed.
   */
  public static interface OnCheckedChangeListener {

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param checkableView The view whose state has changed.
     * @param isChecked The new checked state of checkableView.
     */
    void onCheckedChanged(View checkableView, boolean isChecked);
  }
}
