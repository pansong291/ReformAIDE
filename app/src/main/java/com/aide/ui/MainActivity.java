package com.aide.ui;

import android.app.*;
import android.os.*;
import demonic.reform.*;
import android.view.MenuItem;

public class MainActivity extends Activity
{
  MenuItem item;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    if(item.getTitle().toString().equals("FindViewById"))
     Utils.getDialog(U.lg()).show();
    
    Utils.onPreferenceClick(null, null);
  }
    
  public AIDEEditorPager J8()
  {
    return null;
  }
  
}
