package demonic.reform;

import com.a4455jkjh.colorpicker.ColorPickerDialog;
import android.content.Context;

public class OnColorPickerClickListener implements ColorPickerDialog.a
{
  Context context;
  String key;

  public void setData(Context c, String s)
  {
    context = c;
    key = s;
  }

  @Override
  public void a(int color, String colorStr)
  {
    if(colorStr.length() == 9 && colorStr.startsWith("#00"))
      colorStr = "";
    Utils.getDefSharedPref(context)
      .edit().putString(key, colorStr)
      .commit();
  }
}
