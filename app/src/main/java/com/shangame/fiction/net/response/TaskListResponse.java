package com.shangame.fiction.net.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/3/20
 */
public class TaskListResponse {

    public double cashmoney;
    public List<Cardata> cardata;
    public ArrayList<TaskItem> reddata;
    public ArrayList<TaskItem> daydata;
    public ArrayList<TaskItem> newhanddata;

    public static class Cardata{
        public double price;
        public String headimgurl;
        public String nickname;
    }

}
