package com.calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mainRecycler;
    private DateAdapter dateAdapter;
    private List<Map<String, Object>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.mainBtn).setOnClickListener(this);
        mainRecycler = findViewById(R.id.mainRecycler);
        mainRecycler.setLayoutManager(new GridLayoutManager(this, 7));
        dateAdapter = new DateAdapter(this);
        mainRecycler.setAdapter(dateAdapter);
    }

    private void initData() {
        DateUtil dateUtil = new DateUtil();
        mapList = dateUtil.getThisMonthList();
        for (int i = 0; i < mapList.size(); i++) {
            if (i == 1 || i == 3 || i == 8 || i == 15 || i == 25 || i == 29) {
                Map<String, Object> map = mapList.get(i);
                map.put("isSelect", 1);
            }
        }
        /**
         * 添加头
         */
        String[] dates = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        for (int i = 0; i < 7; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", dates[dates.length - i - 1]);
            map.put("type", 0);
            mapList.add(0, map);
        }
        dateAdapter.addData(mapList);
        dateAdapter.notifyDataSetChanged();
    }

    private void update() {
        Random random = new Random();
        int position = random.nextInt(mapList.size() - 1);
        if (position < 7) return;
        Map<String, Object> map = mapList.get(position);
        int isSelect = (int) map.get("isSelect");
        map.put("isSelect", isSelect == 0 ? 1 : 0);
        dateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        update();
    }
}
