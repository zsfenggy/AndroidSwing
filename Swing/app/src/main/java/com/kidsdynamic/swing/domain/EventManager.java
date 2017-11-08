package com.kidsdynamic.swing.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kidsdynamic.commonlib.utils.ObjectUtils;
import com.kidsdynamic.data.dao.DB_Event;
import com.kidsdynamic.data.dao.EventDao;
import com.kidsdynamic.data.net.event.model.EventWithTodo;
import com.kidsdynamic.data.persistent.DbUtil;
import com.kidsdynamic.data.repository.disk.EventDataStore;
import com.kidsdynamic.data.repository.disk.TodoItemDataStore;
import com.kidsdynamic.swing.R;
import com.kidsdynamic.swing.SwingApplication;
import com.kidsdynamic.swing.model.WatchEvent;
import com.kidsdynamic.swing.model.WatchTodo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * date:   2017/10/26 16:57 <br/>
 */

public class EventManager {
    public static HashMap<String, Integer> eventOptionMap = new HashMap<>(4);
    static {
        eventOptionMap.put(WatchEvent.REPEAT_NEVER, R.string.event_repeat_never);
        eventOptionMap.put(WatchEvent.REPEAT_DAILY, R.string.event_repeat_daily);
        eventOptionMap.put(WatchEvent.REPEAT_WEEKLY, R.string.event_repeat_weekly);
        eventOptionMap.put(WatchEvent.REPEAT_MONTHLY, R.string.event_repeat_monthly);
    }

    public void saveEventForLogin(@NonNull Context context, @NonNull List<EventWithTodo> eventWithTodoList){
        DbUtil dbUtil = DbUtil.getInstance(context.getApplicationContext());

        //首先清空本地数据，然后保存本次
        EventDataStore eventDataStore = new EventDataStore(dbUtil);
        eventDataStore.dealAll();

        TodoItemDataStore todoItemDataStore = new TodoItemDataStore(dbUtil);
        todoItemDataStore.dealAll();

        //save
        eventDataStore.saveAll(BeanConvertor.getDBEventList(eventWithTodoList));
        todoItemDataStore.saveAll(BeanConvertor.getDBTodo(eventWithTodoList));

    }

    public static List<WatchEvent> getEventList(long userId, long startTimeStamp, long endTimeStamp){
        boolean isTestData = true;
        if(isTestData){
            return getTestEventList(startTimeStamp, endTimeStamp);
        }

        List<WatchEvent> result = new ArrayList<>();

        DbUtil dbUtil = DbUtil.getInstance(SwingApplication.getAppContext());
        EventDataStore eventDataStore = new EventDataStore(dbUtil);

        EventDao eventDao = eventDataStore.getDbUtil().getDaoSession().getEventDao();

        //首先查询非repeat，且时间符合的event
        List<WatchEvent> notRepeatEvent = getNotRepeatEvent(userId, startTimeStamp, endTimeStamp, eventDao);
        result.addAll(notRepeatEvent);

        //分别获取Daily, Weekly及Monthly的event；此回傳列表已會展開repeat, 換言之，若有Daily，則會直接
        // 展成一個月的events.
        List<WatchEvent> dailyResult = getEventRepeat(eventDao,userId,startTimeStamp, endTimeStamp, "DAILY");
        List<WatchEvent> weeklyResult = getEventRepeat(eventDao,userId,startTimeStamp, endTimeStamp, "WEEKLY");
        List<WatchEvent> monthlyResult = getEventRepeat(eventDao,userId,startTimeStamp, endTimeStamp, "MONTHLY");

        result.addAll(dailyResult);
        result.addAll(weeklyResult);
        result.addAll(monthlyResult);

        //反序？ 符合UI要求
        Collections.sort(result, new Comparator<WatchEvent>() {
            @Override
            public int compare(WatchEvent t1, WatchEvent t2) {
                if (t2.mAlertTimeStamp > t1.mAlertTimeStamp) {
                    return -1;
                } else if (t2.mAlertTimeStamp < t1.mAlertTimeStamp) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });


        return result;

    }

    private static List<WatchEvent> getNotRepeatEvent(long userId, long startTimeStamp, long endTimeStamp, EventDao eventDao) {
        List<WatchEvent> result = new ArrayList<>();

        QueryBuilder<DB_Event> qb = eventDao.queryBuilder();

        /*rawQuery("SELECT * FROM " + TABLE_EVENT +
                " WHERE " + REPEAT + "=''" + " AND " +
                "((" + startTimeStamp + ">=" + START_DATE + " AND " + startTimeStamp + "<=" + END_DATE + ") OR" +
                " (" + startTimeStamp + "<=" + START_DATE + " AND " + endTimeStamp + ">=" + END_DATE + ") OR" +
                " (" + endTimeStamp + ">=" + START_DATE + " AND " + endTimeStamp + "<=" + END_DATE + "))", null);*/
        List<DB_Event> dbEvents = qb.where(EventDao.Properties.UserId.eq(userId),
                EventDao.Properties.Repeat.eq(""),
                qb.or(qb.and(EventDao.Properties.StartDate.le(startTimeStamp), EventDao.Properties.EndDate.ge(startTimeStamp)),
                        qb.and(EventDao.Properties.StartDate.ge(startTimeStamp), EventDao.Properties.EndDate.le(endTimeStamp))),
                qb.and(EventDao.Properties.StartDate.le(endTimeStamp), EventDao.Properties.EndDate.ge(endTimeStamp))
        ).orderAsc(EventDao.Properties.StartDate).list();

        if(!ObjectUtils.isListEmpty(dbEvents)){
            result.addAll(BeanConvertor.getWatchEvent(dbEvents));
        }

        return result;
    }

    public static List<WatchEvent> getTestEventList(long start, long end){

        List<WatchEvent> list = new ArrayList<>();
        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 28, 8, 30,
                2017, 9, 28, 9, 10,
                WatchEvent.ColorList[0],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz",
                WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);
        list.get(list.size() - 1).mTodoList = Arrays.asList(
                new WatchTodo(1, 452, 0, "1 Todo todo todo todo", WatchTodo.STATUS_DONE),
                new WatchTodo(2, 452, 0, "2 Todo todo todo todo", WatchTodo.STATUS_PENDING)
        );

        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 27, 10, 0, 2017, 9, 27, 10, 50, WatchEvent.ColorList[1],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);

        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 27, 8, 30, 2017, 9, 27, 11, 30, WatchEvent.ColorList[2],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);

        //26 号
        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 26, 10, 30, 2017, 9, 26, 11, 10, WatchEvent.ColorList[3],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);

        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 26, 10, 0, 2017, 9, 26, 10, 50, WatchEvent.ColorList[1],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);

        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 26, 8, 30, 2017, 9, 26, 11, 30, WatchEvent.ColorList[2],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);

        list.add(new WatchEvent(0, 452, "Name",
                2017, 9, 26, 9, 30, 2017, 9, 26, 11, 30, WatchEvent.ColorList[2],
                "Deacription 1234567890 abcdefghijklmnopqrstuvwxyz", WatchEvent.AlarmList[0].mId, WatchEvent.REPEAT_NEVER));
        list.get(list.size() - 1).mKids = Arrays.asList((long)8);


        return list;
    }

    private static List<WatchEvent> getEventRepeat(EventDao eventDao, long userId,
                                                   long startTimeStamp, long endTimeStamp, String repeat){
        List<WatchEvent> resultRaw = new ArrayList<>();
        QueryBuilder<DB_Event> qb = eventDao.queryBuilder();

        /*mDatabase.rawQuery("SELECT * FROM " + TABLE_EVENT +
                " WHERE " +
                REPEAT + "='" + repeat + "'" + " AND " +
                endTimeStamp + ">=" + START_DATE, null);*/

        List<DB_Event> dbEvents = qb.where(EventDao.Properties.Repeat.eq(repeat),
                EventDao.Properties.StartDate.le(endTimeStamp)).
                orderAsc(EventDao.Properties.StartDate).list();

        if(ObjectUtils.isListEmpty(dbEvents)){
           return resultRaw;
        }

        //数据库查询出的重复event
        resultRaw.addAll(BeanConvertor.getWatchEvent(dbEvents));


        //开始展开重复的日历事件，转化成每天，每星期，每月
        List<WatchEvent> result = new ArrayList<>();
        Calendar alertDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        Calendar untilDate = Calendar.getInstance();
        untilDate.setTimeInMillis(alertDate.getTimeInMillis());
        switch (repeat) {
            case WatchEvent.REPEAT_MONTHLY:
                untilDate.add(Calendar.YEAR, 1);
                break;
            case WatchEvent.REPEAT_WEEKLY:
                untilDate.add(Calendar.YEAR, 1);
                break;
            default:
            case WatchEvent.REPEAT_DAILY:
                untilDate.add(Calendar.MONTH, 1);
                break;
        }
        untilDate.add(Calendar.DATE, -1);
        long untilTimeStamp = untilDate.getTimeInMillis();

        for (WatchEvent event : resultRaw) {
            alertDate.setTimeInMillis(event.mAlertTimeStamp);
            startDate.setTimeInMillis(event.mStartDate);
            endDate.setTimeInMillis(event.mEndDate);

            do {
                if (event.mAlertTimeStamp >= startTimeStamp && event.mAlertTimeStamp <= endTimeStamp) {
                    result.add(new WatchEvent(event));
                }

                switch (repeat) {
                    case WatchEvent.REPEAT_DAILY:
                        alertDate.add(Calendar.DATE, 1);
                        startDate.add(Calendar.DATE, 1);
                        endDate.add(Calendar.DATE, 1);
                        break;
                    case WatchEvent.REPEAT_WEEKLY:
                        alertDate.add(Calendar.DATE, 7);
                        startDate.add(Calendar.DATE, 7);
                        endDate.add(Calendar.DATE, 7);
                        break;
                    case WatchEvent.REPEAT_MONTHLY:
                        int day1 = alertDate.get(Calendar.DAY_OF_MONTH);
                        int day2 = 100;

                        while (day1 != day2) {
                            alertDate.add(Calendar.DATE, 1);
                            startDate.add(Calendar.DATE, 1);
                            endDate.add(Calendar.DATE, 1);

                            day2 = alertDate.get(Calendar.DAY_OF_MONTH);
                        }
                        break;
                }
                event.mAlertTimeStamp = alertDate.getTimeInMillis();
                event.mStartDate = startDate.getTimeInMillis();
                event.mEndDate = endDate.getTimeInMillis();
            }
            while (event.mAlertTimeStamp <= endTimeStamp && event.mAlertTimeStamp < untilTimeStamp);
        }

        return result;

    }
}
