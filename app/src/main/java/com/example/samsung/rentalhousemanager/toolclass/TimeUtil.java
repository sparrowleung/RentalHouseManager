package com.example.samsung.rentalhousemanager.toolclass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuyang.liang on 2018/10/15.
 */

public class TimeUtil {

    public static String date = "";
    public static Date dateStart;
    public static Date dateFinish;

    public TimeUtil() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat BdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date = dateFormat.format(new Date(System.currentTimeMillis()));
        String startDate = new StringBuilder(date).append("-01 00:00:00").toString();
        String finishDate = new StringBuilder(date).append("-01 23:59:59").toString();

        try {
            dateStart = BdateFormat.parse(startDate);
            dateFinish = BdateFormat.parse(finishDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
