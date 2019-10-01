package demonic.reform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.a4455jkjh.colorpicker.ColorPickerDialog;
import com.aide.common.ma;
import com.aide.engine.m;
import com.aide.ui.MainActivity;
import java.util.HashMap;
import java.util.Set;

public class Utils
{
  private static final String TAG = Utils.class.getCanonicalName();
  private static AlertDialog dialog;
  private static CheckBox cb_field, cb_private, cb_add_m, cb_root_view, cb_type_conversion;
  private static SharedPreferences sp;
  private static OnColorPickerClickListener onColorPickerClickListener;

  public static SharedPreferences getDefSharedPref(Context c)
  {
    if(sp == null)
      sp = PreferenceManager.getDefaultSharedPreferences(c);
    return sp;
  }

  private static OnColorPickerClickListener getOnColorPickerClickListener(Context c, String key)
  {
    if(onColorPickerClickListener == null)
      onColorPickerClickListener = new OnColorPickerClickListener();
    onColorPickerClickListener.setData(c, key);
    return onColorPickerClickListener;
  }

  public static void onPreferenceClick(Activity activity, Preference p)
  {
    String key = p.getKey();
    if(!key.startsWith("editor_syntax_"))
      return;
    int color = 0;
    boolean errorColor = false;
    try
    {
      color = Color.parseColor(getDefSharedPref(activity).getString(key, null));
    }catch(Exception e)
    {
      errorColor = true;
    }
    if(errorColor)
    {
      key = key.substring(14);
      int endIndex = key.lastIndexOf("_light");
      boolean isLight = endIndex >= 0;
      if(isLight) key = key.substring(0, endIndex);
      switch(key)
      {
        case "plain":
          color = m.j6.getDefColor(activity, isLight);
          break;
        case "keyword":
          color = m.DW.getDefColor(activity, isLight);
          break;
        case "identifier":
          color = m.FH.getDefColor(activity, isLight);
          break;
        case "operator":
          color = m.VH.getDefColor(activity, isLight);
          break;
        case "literal":
          color = m.u7.getDefColor(activity, isLight);
          break;
        case "preprocessor":
          color = m.tp.getDefColor(activity, isLight);
          break;
        case "comment":
          color = m.EQ.getDefColor(activity, isLight);
          break;
        case "script":
          color = m.J8.getDefColor(activity, isLight);
          break;
        case "type_identifier":
          color = m.v5.getDefColor(activity, isLight);
          break;
        case "delegate_identifier":
          color = m.Zo.getDefColor(activity, isLight);
          break;
        case "documentation_comment":
          color = m.we.getDefColor(activity, isLight);
          break;
        case "script_background":
          color = m.J0.getDefColor(activity, isLight);
          break;
        case "package_identifier":
          color = m.Hw.getDefColor(activity, isLight);
          break;
        case "separator":
          color = m.gn.getDefColor(activity, isLight);
          break;
      }
    }
    ma.j6(activity,
          new ColorPickerDialog(p.getTitle().toString(), color,
                                getOnColorPickerClickListener(activity, p.getKey())));
  }

  public static int getSyntaxColor(Context c, String type, boolean isLight, int defColor)
  {
    String key = "editor_syntax_";
    switch(type)
    {
      case "Plain":
      case "Keyword":
      case "Identifier":
      case "Operator":
      case "Literal":
      case "Preprocessor":
      case "Comment":
      case "Script":
      case "Type Identifier":
      case "Delegate Identifier":
      case "Documentation Comment":
      case "Script Background":
        key += type.replace(" ", "_").toLowerCase();
        break;

      case "Namespace/Package Identifier":
        key += "package_identifier";
        break;

      case "Separator/Punctuator":
        key += "separator";
        break;
    }
    if(isLight) key += "_light";
    try
    {
      defColor = Color.parseColor(getDefSharedPref(c).getString(key, null));
    }catch(Exception e)
    {
    }
    return defColor;
  }

  public static AlertDialog getDialog(MainActivity activity)
  {
    if(dialog == null)
      dialog = new AlertDialog.Builder(activity)
        .setView(getDialogView(activity))
        .setPositiveButton(
        "Ok", new DialogInterface.OnClickListener()
        {
          MainActivity activity;

          public DialogInterface.OnClickListener setData(MainActivity ma)
          {
            activity = ma;
            return this;
          }

          @Override
          public void onClick(DialogInterface p1, int p2)
          {
            ClipboardManager cm = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(getJavaCode(activity.J8().getSelectionContent()));
            Toast.makeText(activity, "The content has been copied to clipboard", 1).show();
          }
        }.setData(activity))
        .create();
    return dialog;
  }

  private static View getDialogView(Activity activity)
  {
    LinearLayout llt = new LinearLayout(activity);
    llt.setOrientation(LinearLayout.VERTICAL);
    cb_field = new CheckBox(activity);
    cb_private = new CheckBox(activity);
    cb_add_m = new CheckBox(activity);
    cb_root_view = new CheckBox(activity);
    cb_type_conversion = new CheckBox(activity);
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    llt.addView(cb_field, lp);
    llt.addView(cb_private, lp);
    llt.addView(cb_add_m, lp);
    llt.addView(cb_root_view, lp);
    llt.addView(cb_type_conversion, lp);
    cb_field.setText("Field");
    cb_private.setText("Private");
    cb_add_m.setText("Add m");
    cb_root_view.setText("Root view");
    cb_type_conversion.setText("Type conversion");
    llt.setPadding(24, 24, 0, 0);
    return llt;
  }

  private static String getJavaCode(String xmlCode)
  {
    log(TAG, "XML CODE:\n" + xmlCode);
    if(xmlCode == null) return null;
    HashMap<String, String> map = new HashMap<>();
    StringBuilder tag = new StringBuilder();
    StringBuilder attr = new StringBuilder();
    StringBuilder value = new StringBuilder();
    char currentChar;
    boolean startRecord = false;
    for(int i = 0; i < xmlCode.length(); i++)
    {
      currentChar = xmlCode.charAt(i);
      switch(currentChar)
      {
        case '<':
          tag.delete(0, tag.length());
          for(; ++i < xmlCode.length();)
          {
            currentChar = xmlCode.charAt(i);
            if(Character.isWhitespace(currentChar))
            {
              if(startRecord)
              {
                startRecord = false;
                break;
              }
            }else
            {
              startRecord = true;
              tag.append(currentChar);
            }
          }
          int pointIndex = tag.lastIndexOf(".");
          if(pointIndex >= 0)
          {
            tag.delete(0, pointIndex + 1);
          }
          log(TAG, "tag:<" + tag.toString());
          break;

        case '"':
          value.delete(0, value.length());
          for(; ++i < xmlCode.length();)
          {
            currentChar = xmlCode.charAt(i);
            if(currentChar == '"')
            {
              if(startRecord)
              {
                startRecord = false;
                break;
              }
            }else
            {
              startRecord = true;
              value.append(currentChar);
              if(currentChar == '\\')
              {
                value.append(xmlCode.charAt(++i));
              }
            }
          }
          if(attr.toString().equals("android:id"))
          {
            map.put(value.toString(), tag.toString());
          }
          log(TAG, "=\"" + value.toString() + "\"");
          break;

        default:
          if(Character.isLetter(currentChar))
          {
            attr.delete(0, attr.length());
            for(; i < xmlCode.length(); i++)
            {
              currentChar = xmlCode.charAt(i);
              if(currentChar == '=')
              {
                if(startRecord)
                {
                  startRecord = false;
                  break;
                }
              }else if(!Character.isWhitespace(currentChar))
              {
                startRecord = true;
                attr.append(currentChar);
              }
            }
            log(TAG, "\t" + attr.toString());
          }
      }
    }
    Set keySet = map.keySet();
    StringBuilder fields = new StringBuilder();
    StringBuilder method = new StringBuilder();
    method.append("private void initView(");

    // need add a root view ?
    if(cb_root_view.isChecked()) method.append("View view");
    method.append(")\n{\n");
    boolean isAndroidId;
    String clazz;
    String varName;
    for(String id: keySet)
    {
      // class
      clazz = map.get(id);
      log(TAG, "class: " + clazz + ", id: " + id);

      // id
      isAndroidId = id.startsWith("@android:id/");
      int divisionSignIndex = id.indexOf("/");
      if(divisionSignIndex >= 0)
        id = id.substring(divisionSignIndex + 1);

      // variable name need add a prefix m ?
      if(cb_add_m.isChecked())
      {
        StringBuilder vN = new StringBuilder();
        String[] words = id.split("_");
        for(int i = 0; i < words.length; i++)
        {
          vN.append(Character.toUpperCase(words[i].charAt(0)));
          if(words[i].length() > 1)
            vN.append(words[i].substring(1));
        }
        varName = "m" + vN.toString();
      }else
        varName = id;

      // is private ?
      if(cb_private.isChecked()) fields.append("private ");
      fields.append(clazz + " " + varName + ";\n");
      method.append('\t');

      // don't create a field ?
      if(!cb_field.isChecked()) method.append(clazz + " ");
      method.append(varName + " = ");

      // need add a type conversion ?
      if(cb_type_conversion.isChecked())
        method.append("(" + clazz + ") ");

      // need add a root view ?
      if(cb_root_view.isChecked()) method.append("view.");
      method.append("findViewById(");

      // is default id of android ?
      if(isAndroidId) method.append("android.");
      method.append("R.id." + id + ");\n");
    }
    fields.append("\n");
    method.append("}");
    varName = method.toString();
    // need create fields ?
    if(cb_field.isChecked())
      varName = fields.toString() + varName;
    log(TAG, varName);
    return varName;
  }

  public static void log(String tag, String s)
  {
    System.out.println(tag + ", " + s);
  }

  public static CharSequence getPackagePrefix(Context c, CharSequence suffix)
  {
    String s = getDefSharedPref(c).getString("package_prefix", null);
    if(s == null || s.isEmpty()) s = "com.mycompany.";
    if(!s.endsWith(".")) s += ".";
    s += suffix.toString().toLowerCase();
    return s;
  }
}
