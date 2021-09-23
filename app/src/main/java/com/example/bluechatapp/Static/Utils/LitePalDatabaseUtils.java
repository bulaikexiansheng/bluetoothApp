package com.example.bluechatapp.Static.Utils;

import org.litepal.LitePal;

public class LitePalDatabaseUtils {
    public static final LitePalDatabaseUtils litePalDatabaseUtil = new LitePalDatabaseUtils() ;
    public boolean getDataBase(){
        LitePal.getDatabase() ;
        return true ;
    }
}
