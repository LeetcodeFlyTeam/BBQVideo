package com.flyteam.player;

public class BBQPlayer {
   static {
       System.loadLibrary("native-lib");
   }

   public native void setSource(String source);
}
