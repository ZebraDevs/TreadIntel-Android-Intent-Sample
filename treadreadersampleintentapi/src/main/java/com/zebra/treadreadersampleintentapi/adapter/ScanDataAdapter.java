package com.zebra.treadreadersampleintentapi.adapter;

import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_HIGH;
import static com.zebra.treadreadersampleintentapi.util.Constants.NEUTRON_DEPTH_LOW;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.zebra.treadreadersampleintentapi.R;
import com.zebra.treadreadersampleintentapi.models.ScanData;
import com.zebra.treadreadersampleintentapi.models.TreadData;
import com.zebra.treadreadersampleintentapi.models.TreadDataList;
import com.zebra.treadreadersampleintentapi.util.Utils;

import java.util.ArrayList;

public class ScanDataAdapter extends RecyclerView.Adapter<ScanDataAdapter.ViewHolder> {

    ArrayList<ScanData> scanData;
    Context mContext;

    public ScanDataAdapter(ArrayList<ScanData> scanData,Context context) {
        this.scanData = scanData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.scan_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTireId.setText(scanData.get(position).getTireId());
        setDataForUi(scanData.get(position).getJsonData(),
                holder.tvInnerDepth,holder.tvCenterDepth,holder.tvOuterDepth,
                holder.indicatorInner1,holder.indicatorInner2,holder.indicatorInner3,
                holder.indicatorCenter1,holder.indicatorCenter2,holder.indicatorCenter3,
                holder.indicatorOuter1,holder.indicatorOuter2,holder.indicatorOuter3);
    }

    @Override
    public int getItemCount() {
        return scanData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTireId,tvInnerDepth,tvCenterDepth,tvOuterDepth;
        ImageView indicatorInner1,indicatorInner2,indicatorInner3,indicatorCenter1,indicatorCenter2,indicatorCenter3,indicatorOuter1,indicatorOuter2,indicatorOuter3;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTireId = itemView.findViewById(R.id.tvTireId);
            tvInnerDepth = itemView.findViewById(R.id.tvInnerDepth);
            tvCenterDepth = itemView.findViewById(R.id.tvCenterDepth);
            tvOuterDepth = itemView.findViewById(R.id.tvOuterDepth);
            indicatorInner1 = itemView.findViewById(R.id.indicatorInner1);
            indicatorInner2 = itemView.findViewById(R.id.indicatorInner2);
            indicatorInner3 = itemView.findViewById(R.id.indicatorInner3);
            indicatorCenter1 = itemView.findViewById(R.id.indicatorCenter1);
            indicatorCenter2 = itemView.findViewById(R.id.indicatorCenter2);
            indicatorCenter3 = itemView.findViewById(R.id.indicatorCenter3);
            indicatorOuter1 = itemView.findViewById(R.id.indicatorOuter1);
            indicatorOuter2 = itemView.findViewById(R.id.indicatorOuter2);
            indicatorOuter3 = itemView.findViewById(R.id.indicatorOuter3);
        }
    }


    private void setDataForUi(String jsonData,
                              TextView tvInnerDepth,TextView tvCenterDepth,TextView tvOuterDepth,
                              ImageView indicatorInner1,ImageView indicatorInner2,ImageView indicatorInner3,
                              ImageView indicatorCenter1,ImageView indicatorCenter2,ImageView indicatorCenter3,
                              ImageView indicatorOuter1,ImageView indicatorOuter2,ImageView indicatorOuter3){
        Gson gson = new Gson();
        TreadDataList treadDataList  = gson.fromJson(jsonData, TreadDataList.class);
        for (int i = 0; i < treadDataList.getTreadList().size(); i++) {
            String position = treadDataList.getTreadList().get(i).getPosition();
            TreadData treadData = treadDataList.getTreadList().get(i);
            switch (position) {
                case "Inner":
                    tvInnerDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorInner1,indicatorInner2,indicatorInner3,treadData.getMetricTreadDepth());
                    break;
                case "Center":
                    tvCenterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorCenter1,indicatorCenter2,indicatorCenter3,treadData.getMetricTreadDepth());
                    break;
                case "Outer":
                    tvOuterDepth.setText(getFormattedData(Float.parseFloat(Utils.mmTo32nds(treadData.getMetricTreadDepth()))));
                    showIndicators(indicatorOuter1,indicatorOuter2,indicatorOuter3,treadData.getMetricTreadDepth());
                    break;
            }
        }
    }
    private String getFormattedData(float value){
        return String.format("%.1f", value);
    }

    private void showIndicators(ImageView indicator1, ImageView indicator2, ImageView indicator3, float treadDepth){
        if(treadDepth < NEUTRON_DEPTH_LOW) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.INVISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator3.setBackgroundColor(mContext.getColor(R.color.low));
        } else if((treadDepth >= NEUTRON_DEPTH_LOW) && (treadDepth < NEUTRON_DEPTH_HIGH)) {
            indicator1.setVisibility(View.INVISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator2.setBackgroundColor(mContext.getColor(R.color.mid2));
            indicator3.setBackgroundColor(mContext.getColor(R.color.mid));
        } else if(treadDepth >= NEUTRON_DEPTH_HIGH) {
            indicator1.setVisibility(View.VISIBLE);
            indicator2.setVisibility(View.VISIBLE);
            indicator3.setVisibility(View.VISIBLE);
            indicator1.setBackgroundColor(mContext.getColor(R.color.high3));
            indicator2.setBackgroundColor(mContext.getColor(R.color.high2));
            indicator3.setBackgroundColor(mContext.getColor(R.color.high));
        }
    }
}
