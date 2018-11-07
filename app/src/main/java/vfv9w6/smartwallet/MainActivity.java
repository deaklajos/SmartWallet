package vfv9w6.smartwallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Chart contents

        setTitle("BarChartPositiveNegative");

        chart = findViewById(R.id.chart1);
        chart.setBackgroundColor(android.R.attr.background);
        chart.setExtraTopOffset(-30f);
        chart.setExtraBottomOffset(10f);
        chart.setExtraLeftOffset(70f);
        chart.setExtraRightOffset(70f);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDragYEnabled(false);
        chart.setScaleYEnabled(false);

        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfRegular);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        data.add(new Data(0f, -224.1f, "12-29"));
        data.add(new Data(1f, 238.5f, "12-30"));
        data.add(new Data(2f, 1280.1f, "12-31"));
        data.add(new Data(3f, -442.3f, "01-01"));
        data.add(new Data(4f, -2280.1f, "01-02"));
        data.add(new Data(5f, -224.1f, "01-03"));
        data.add(new Data(6f, 238.5f, "01-04"));
        data.add(new Data(7f, 1280.1f, "01-05"));
        data.add(new Data(8f, -442.3f, "01-06"));
        data.add(new Data(9f, -2280.1f, "01-07"));
        data.add(new Data(10f, -224.1f, "12-29"));
        data.add(new Data(11f, 238.5f, "12-30"));
        data.add(new Data(12f, 1280.1f, "12-31"));
        data.add(new Data(13f, -442.3f, "01-01"));
        data.add(new Data(14f, -2280.1f, "01-02"));
        data.add(new Data(15f, -224.1f, "01-03"));
        data.add(new Data(16f, 238.5f, "01-04"));
        data.add(new Data(17f, 1280.1f, "01-05"));
        data.add(new Data(18f, -442.3f, "01-06"));
        data.add(new Data(19f, -2280.1f, "01-07"));
        data.add(new Data(20f, -224.1f, "12-29"));
        data.add(new Data(21f, 238.5f, "12-30"));
        data.add(new Data(22f, 1280.1f, "12-31"));
        data.add(new Data(23f, -442.3f, "01-01"));
        data.add(new Data(24f, -2280.1f, "01-02"));
        data.add(new Data(25f, -224.1f, "01-03"));
        data.add(new Data(26f, 238.5f, "01-04"));
        data.add(new Data(27f, 1280.1f, "01-05"));
        data.add(new Data(28f, -442.3f, "01-06"));
        data.add(new Data(29f, -2280.1f, "01-07"));

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return data.get(Math.min(Math.max((int) value, 0), data.size()-1)).xAxisValue;
            }
        });

        setData(data);

        // Set range and start position.
        chart.setVisibleXRangeMaximum(10);
        chart.setVisibleXRangeMinimum(2);
        chart.moveViewToX(Float.MAX_VALUE);
    }

    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(green);
            else
                colors.add(red);
        }

        BarDataSet set;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            data.setBarWidth(0.8f);

            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * Demo class representing data.
     */
    private class Data {

        final String xAxisValue;
        final float yValue;
        final float xValue;

        Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
