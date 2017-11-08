
package com.agenthun.eseal.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.agenthun.eseal.R;
import com.agenthun.eseal.bean.base.LocationDetail;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your listview-item
 * 
 * @author Philipp Jahoda
 */
public class ListViewLineChartActivity extends FragmentActivity {
    private static final String TAG = "ListViewLineChartActivity";

    public static Intent newIntent(Context context, ArrayList<LocationDetail> details) {
        Intent i = new Intent(context, ListViewLineChartActivity.class);
        i.putParcelableArrayListExtra(TAG, details);

        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listview_chart);
        
        ListView lv = (ListView) findViewById(R.id.listView1);

        ArrayList<LocationDetail> details = getIntent().getParcelableArrayListExtra(TAG);

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), getLineData(details));
        lv.setAdapter(cda);
    }

    @NonNull
    private ArrayList<LineData> getLineData(ArrayList<LocationDetail> details) {
        ArrayList<LineData> list = new ArrayList<LineData>();

        // Real data
        list.add(getTemperatureData(details));
        list.add(getHumidityData(details));
        list.add(getVibrationXData(details));
        list.add(getVibrationYData(details));
        list.add(getVibrationZData(details));

        return list;
    }

    private LineData getTemperatureData(ArrayList<LocationDetail> details) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for(int i = 0; i < details.size(); i++) {
            entries.add(new Entry(i, details.get(i).getTemperature()));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(4.5f);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
        iLineDataSets.add(lineDataSet);

        LineData cd = new LineData(iLineDataSets);
        return cd;
    }

    private LineData getHumidityData(ArrayList<LocationDetail> details) {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for(int i = 0; i < details.size(); i++) {
            entries.add(new Entry(i, details.get(i).getHumidity()));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "湿度");
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(4.5f);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(Color.rgb(255, 208, 140));
        lineDataSet.setCircleColor(Color.rgb(255, 208, 140));
        lineDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
        iLineDataSets.add(lineDataSet);

        LineData cd = new LineData(iLineDataSets);
        return cd;
    }

    private LineData getVibrationXData(ArrayList<LocationDetail> details) {
        ArrayList<Entry> xEntries = new ArrayList<Entry>();

        for(int i = 0; i < details.size(); i++) {
            xEntries.add(new Entry(i, details.get(i).getVibrationX()));
        }

        LineDataSet xLineDataSet = new LineDataSet(xEntries, "振动X");
        xLineDataSet.setLineWidth(1f);
        xLineDataSet.setCircleRadius(4.5f);
        xLineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        xLineDataSet.setColor(Color.rgb(255, 247, 140));
        xLineDataSet.setCircleColor(Color.rgb(255, 247, 140));
        xLineDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
        iLineDataSets.add(xLineDataSet);

        LineData cd = new LineData(iLineDataSets);
        return cd;
    }

    private LineData getVibrationYData(ArrayList<LocationDetail> details) {
        ArrayList<Entry> yEntries = new ArrayList<Entry>();

        for(int i = 0; i < details.size(); i++) {
            yEntries.add(new Entry(i, details.get(i).getVibrationY()));
        }

        LineDataSet yLineDataSet = new LineDataSet(yEntries, "振动Y");
        yLineDataSet.setLineWidth(1f);
        yLineDataSet.setCircleRadius(4.5f);
        yLineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        yLineDataSet.setColor(Color.rgb(192, 255, 140));
        yLineDataSet.setCircleColor(Color.rgb(192, 255, 140));
        yLineDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
        iLineDataSets.add(yLineDataSet);

        LineData cd = new LineData(iLineDataSets);
        return cd;
    }

    private LineData getVibrationZData(ArrayList<LocationDetail> details) {
        ArrayList<Entry> zEntries = new ArrayList<Entry>();

        for(int i = 0; i < details.size(); i++) {
            zEntries.add(new Entry(i, details.get(i).getVibrationZ()));
        }

        LineDataSet zLineDataSet = new LineDataSet(zEntries, "振动Z");
        zLineDataSet.setLineWidth(1f);
        zLineDataSet.setCircleRadius(4.5f);
        zLineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        zLineDataSet.setColor(Color.rgb(193, 37, 82));
        zLineDataSet.setCircleColor(Color.rgb(193, 37, 82));
        zLineDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();
        iLineDataSets.add(zLineDataSet);

        LineData cd = new LineData(iLineDataSets);
        return cd;
    }

    private class ChartDataAdapter extends ArrayAdapter<LineData> {

        public ChartDataAdapter(Context context, List<LineData> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LineData data = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item_linechart, null);
                holder.chart = (LineChart) convertView.findViewById(R.id.chart);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            if (!holder.chart.isEmpty()) {
//                holder.chart.clearValues();
//            }
//            holder.chart.clear();

            // apply styling
            // holder.chart.setValueTypeface(mTf);
            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);

            YAxis leftAxis = holder.chart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setLabelCount(5, false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            // set data
            holder.chart.setData((LineData) data);

            // do not forget to refresh the chart
            holder.chart.invalidate();

            holder.chart.animateX(750);

            return convertView;
        }

        private class ViewHolder {

            LineChart chart;
        }
    }
}
