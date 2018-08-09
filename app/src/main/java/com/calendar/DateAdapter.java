package com.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> mapList;

    public DateAdapter(Context context) {
        this.context = context;
    }

    /**
     * @param mapList type 0周排行 1上个月 2本月 3下个月
     *                isSelect 是否选中 1是 0否
     *                date 实际日期
     */
    public void addData(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_week, null, false);
            WeekHolder weekHolder = new WeekHolder(view);
            return weekHolder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_date, null);
            DateHolder dateHolder = new DateHolder(view);
            return dateHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, Object> map = mapList.get(position);
        switch (getItemViewType(position)) {
            case 0:
                WeekHolder weekHolder = (WeekHolder) holder;
                String week = (String) map.get("date");
                weekHolder.adapter_week.setText(week);
                break;
            case 2:
                DateHolder dateHolder = (DateHolder) holder;
                dateHolder.adapter_date_rl.setBackgroundResource(R.drawable.date_frame_white);
                String date = (String) map.get("date");
                String[] dates = date.split("-");
                dateHolder.adapter_date.setText(dates[2]);
                int isSelect = (int) map.get("isSelect");
                if (isSelect == 1) {
                    dateHolder.adapter_date_sign.setVisibility(View.VISIBLE);
                } else {
                    dateHolder.adapter_date_sign.setVisibility(View.GONE);
                }
                break;
            default:
                DateHolder dateHolderLF = (DateHolder) holder;
                dateHolderLF.adapter_date_rl.setBackgroundResource(R.drawable.date_frame_green);
                String dateLF = (String) map.get("date");
                String[] datesLF = dateLF.split("-");
                dateHolderLF.adapter_date.setText(datesLF[2]);
                dateHolderLF.adapter_date.setTextColor(Color.parseColor("#a4a6a4"));
                int isSelectLF = (int) map.get("isSelect");
                if (isSelectLF == 1) {
                    dateHolderLF.adapter_date_sign.setVisibility(View.VISIBLE);
                } else {
                    dateHolderLF.adapter_date_sign.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mapList == null ? 0 : mapList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (int) mapList.get(position).get("type");
    }

    class DateHolder extends RecyclerView.ViewHolder {
        TextView adapter_date;
        ImageView adapter_date_sign;
        RelativeLayout adapter_date_rl;

        public DateHolder(View itemView) {
            super(itemView);
            adapter_date_sign = itemView.findViewById(R.id.adapter_date_sign);
            adapter_date = itemView.findViewById(R.id.adapter_date);
            adapter_date_rl = itemView.findViewById(R.id.adapter_date_rl);
        }
    }

    class WeekHolder extends RecyclerView.ViewHolder {
        TextView adapter_week;
        RelativeLayout adapter_week_rl;

        public WeekHolder(View itemView) {
            super(itemView);
            adapter_week = itemView.findViewById(R.id.adapter_week);
            adapter_week_rl = itemView.findViewById(R.id.adapter_week_rl);
        }
    }
}
