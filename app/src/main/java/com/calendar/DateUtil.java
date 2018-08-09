package com.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据calendar类制作获取日历
 * 所有的当月指calendar持有的月份
 */
public class DateUtil {
    private Calendar calendar;
    private int j = 0;//向上，向下的月份记录
    private boolean isLastMore;//以周日为起始，月初第一天为周日，是否多显示上一月的七天
    private boolean isNextMore;//以周日为起始，月初第一天为周六，是否多显示下一月的七天
    private boolean isShowLastAndNext = true;//是否显示上月盈余和下月空缺

    public DateUtil() {
        calendar = Calendar.getInstance();
        init();
    }

    public int getJ() {
        return j;
    }

    public void setLastMore(boolean lastMore) {
        isLastMore = lastMore;
    }

    public void setNextMore(boolean nextMore) {
        isNextMore = nextMore;
    }

    public void setShowLastAndNext(boolean showLastAndNext) {
        isShowLastAndNext = showLastAndNext;
    }

    public void init() {
        Date curDate = new Date();
        calendar.setTime(curDate);
        j = 0;
    }

    /**
     * @param num 调节月份
     */
    public void adjustMonth(int num) {
        calendar.add(Calendar.MONTH, num);
        j += num;
    }

    /**
     * 获取本月天数
     *
     * @return
     */
    public int getDaysOfMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        return monthDay;
    }

    /**
     * @return 获取本月第一天周几
     * 0-6 -> 日 - 一 - 六
     */
    public int getWeekOfFirstDayByMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayInWeek - 1;
    }

    /**
     * @return 获取本月最后一天周几
     * 0-6 -> 日 - 一 - 六
     */
    public int getWeekOfFinalDayByMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayInWeek - 1;
    }

    /**
     * 添加是否处理操作 isSelect 默认为0
     *
     * @return 获取日历显示的当前月份，包括上下月的余天数
     */
    public List<Map<String, Object>> getThisMonthList() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        int thisMonthDays = getDaysOfMonth();
//        type 1上个月 2本月 3下个月
        //当月的年月 yyyy-MM-dd
        String ym = dateToString("yyyy-MM", calendar.getTime());
        for (int i = 1; i <= thisMonthDays; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 2);
            map.put("date", ym + "-" + (i < 10 ? "0" + i : i));
            map.put("isSelect", 0);
            mapList.add(map);
        }
        if (!isShowLastAndNext) return mapList;

        return lackOfLastAndNextMonthDays(mapList);
    }

    /**
     * 添补上月盈余和下月空缺
     * 添加是否处理操作 isSelect 默认为0
     *
     * @return 当月展示的所有信息
     */
    public List<Map<String, Object>> lackOfLastAndNextMonthDays(List<Map<String, Object>> mapList) {
        /**
         * 先处理上月盈余，再处理下月空缺
         */
        int firstWeek = getWeekOfFirstDayByMonth();
        int finalWeek = getWeekOfFinalDayByMonth();
        int lackOfLastMonthDays = firstWeek;//本月显示的，需要填补的上一月的天数
        int lackOfNextMonthDays = 6 - finalWeek;//本月显示的，需要填补的下一月的天数
        if (isLastMore) {
            lackOfLastMonthDays += 7;
        }
        if (isNextMore) {
            lackOfNextMonthDays += 7;
        }
        adjustMonth(-1);
        int thisMonthDays = getDaysOfMonth();
//        type 1上个月 2本月 3下个月
        //当月上一月的年月 yyyy-MM-dd
        String ym = dateToString("yyyy-MM", calendar.getTime());
        for (int i = lackOfLastMonthDays; i > 0; i--) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 1);
            map.put("date", ym + "-" + thisMonthDays--);
            map.put("isSelect", 0);
            mapList.add(0, map);
        }
        adjustMonth(+2);
        ym = dateToString("yyyy-MM", calendar.getTime());
        for (int i = 1; i <= lackOfNextMonthDays; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 3);
            map.put("date", ym + "-" + (i < 10 ? "0" + i : i));
            map.put("isSelect", 0);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 日期转换成对应格式的时间
     *
     * @param format yyyy-MM-dd
     */
    private String dateToString(String format, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(time != -1 ? time : System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private String dateToString(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}

