package com.flyteam.bbqvideo.util;
public abstract class StringUtils {
    static public boolean isEmpty(String str){
        return str == null || "".equals(str);
    }
}
