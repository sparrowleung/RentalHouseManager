package com.example.samsung.rentalhousemanager.main;

import android.support.annotation.NonNull;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.message.MsgSendFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/6.
 */

public enum MainType {
    HOME(R.string.homepage, HomeFragment.class, R.drawable.house),
    NewsTips(R.string.news_and_tips, MsgSendFragment.class, R.drawable.lights);

    final int mainName;
    final Class mainClass;
    final int mainIcon;

    MainType(int name, Class clas, int icon) {
        mainName = name;
        mainClass = clas;
        mainIcon = icon;
    }

    public static int getName(MainType mainType) {
        return mainType.mainName;
    }

    public static Class getMainClass(MainType mainType) {
        return mainType.mainClass;
    }

    public static int getIcon(MainType mainType) {
        return mainType.mainIcon;
    }

    public static Map<String, Object> getDrawerItemMap(MainType mainType, int order) {
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> childList;

        itemMap.put("name", getName(mainType));
        itemMap.put("icon", getIcon(mainType));

        switch (mainType) {
            case HOME:
                itemMap.put("type", "main");
                break;
            case NewsTips:
                itemMap.put("type", "main");
                break;
        }

        if (itemMap.containsKey("type") && itemMap.get("type").equals("tab")) {
            itemMap.put("order", order);
        }

        return itemMap;
    }

    public boolean contains(@NonNull String name) {
        if (name.isEmpty()) {
            return false;
        }
        for (MainType type : values()) {
            if (type.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
