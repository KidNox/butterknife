package butterknife.internal;

import android.view.View;

/**
 * A {@linkplain View.OnClickListener click listener} that debounces multiple clicks posted in the
 * same frame. A click on one button disables all buttons for that frame.
 */
public abstract class DebouncingOnClickListener implements View.OnClickListener {

  private static boolean enabled = true;

  private static ClickCondition clickCondition;

  public static void setClickCondition(ClickCondition _clickCondition) {
    clickCondition = _clickCondition;
  }

  private static boolean canClick() {
    return clickCondition == null || clickCondition.allowClick();
  }

  private static final Runnable ENABLE_AGAIN = new Runnable() {
    @Override public void run() {
      enabled = true;
    }
  };

  @Override public final void onClick(View v) {
    if (enabled && canClick()) {
      enabled = false;
      v.post(ENABLE_AGAIN);
      doClick(v);
    }
  }

  public abstract void doClick(View v);


  public interface ClickCondition {
    boolean allowClick();
  }

}
