package com.aide.engine;

import android.content.Context;
import demonic.reform.Utils;

public enum m
{
  j6, DW, FH, Hw,
  v5, Zo, VH,
  gn, u7, tp, EQ,
  we, J0, J8;
  
  String U2;
  int j3, aM;
  
  public int j6(Context context, boolean z)
  {
    return Utils.getSyntaxColor(context, U2, z, getDefColor(context, z));
  }
  
  public int getDefColor(Context context, boolean z)
  {
    return context.getResources().getColor(z ? this.j3 : this.aM);
  }
  
}
