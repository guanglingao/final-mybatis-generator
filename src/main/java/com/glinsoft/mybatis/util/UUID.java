package com.glinsoft.mybatis.util;


public class UUID {

    public synchronized static long getUUID(){
        return System.currentTimeMillis();
    }

}
