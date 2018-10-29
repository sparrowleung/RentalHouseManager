package com.example.samsung.rentalhousemanager.main;

import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/16.
 */

public interface IHomeView {
    void setFloor(List<Map<String, Integer>> list);
    void setChildList(List<List<Map<String, Integer>>> list);
    void setExpandList();
    void getMsgFail();
}
