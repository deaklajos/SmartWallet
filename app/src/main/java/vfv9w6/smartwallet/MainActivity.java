package vfv9w6.smartwallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import vfv9w6.smartwallet.asynctask.LoadDataBaseAsyncTask;
import vfv9w6.smartwallet.fragment.CreateFragment;
import vfv9w6.smartwallet.model.Money;

public class MainActivity extends AppCompatActivity implements CreateFragment.AddListener, OnChartValueSelectedListener {

    private BarChart chart;
    private int greenColor;
    private int redColor;
    public static final String MONEY_KEY = "MONEY";
    private List<Money> list;

    public void setList(List<Money> list) {
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greenColor = ResourcesCompat.getColor(getResources(), R.color.colorGraphGreen, null);
        redColor = ResourcesCompat.getColor(getResources(), R.color.colorGraphRed, null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFragment fragment = new CreateFragment();
                fragment.show(getFragmentManager(), "Create");
            }
        });

        chart = findViewById(R.id.chart1);

        // Load possibly large amount of data asynchronously.
        new LoadDataBaseAsyncTask(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Deselect element
        chart.highlightValue(1, -1);
    }

    public void initChart() {
        // Chart contents
        chart.setOnChartValueSelectedListener(this);
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

        if(list.size() == 0)
        {
            Date today = new Date();
            today = new Date(today.getTime() - ((long)1000 * 60 * 60 * 24 * 30 * (3 + 1)));

            Random rnd = new Random();
            for(int i = 0; i < 30; i++)
            {
                Money pocket_money = new Money(5000 + rnd.nextInt(1000),
                        Money.Type.POCKET_MONEY, "Money from home",
                        today = new Date(today.getTime() + (1000 * 60 * 60 * 24)),
                        47.0,19.0);

                pocket_money.save();
                list.add(pocket_money);

                for(int j = 0; j < 3; j++)
                {
                    Money spend = new Money(-1000 - rnd.nextInt(1000),
                            Money.Type.GROCERIES, "Food",
                            today = new Date(today.getTime() + (1000 * 60 * 60 * 24)),
                            47.0,19.0);

                    spend.save();
                    list.add(spend);
                }
            }
        }

        final List<Data> data = new ArrayList<>();
        float i = 0f;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Money money: list)
        {
            data.add(new Data(i++, money.money, df.format(money.date)));
        }
        // TODO format axis
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
        chart.animateY(250);
    }

    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(greenColor);
            else
                colors.add(redColor);
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

    private void addMoney(Money money)
    {
        // If a single element insert takes a very long time,
        // then the problems may be somewhere else.
        money.save();

        list.add(money);
        BarEntry entry = new BarEntry(chart.getBarData().getEntryCount(), money.money);
        BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(0);
        set.addEntry(entry);
        if (money.money >= 0)
            set.addColor(greenColor);
        else
            set.addColor(redColor);
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    public void deleteMoney(Money money)
    {
        // If a single element delete takes a very long time,
        // then the problems may be somewhere else.
        Money.delete(money);

        int index = list.indexOf(money);
        list.remove(money);
        BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(0);
        set.removeEntry(index);
        set.getColors().remove(index);
        for (int i = index; i < set.getEntryCount(); i++)
        {
            Entry entry = set.getEntryForIndex(i);
            entry.setX(entry.getX() - 1);
        }
        set.notifyDataSetChanged();
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    @Override
    public void onAdd(Money money) {
        addMoney(money);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(0);
        int index = set.getEntryIndex(e);
        Money money = list.get(index);

        Intent intent = new Intent(getBaseContext(), MoneyActivity.class);
        intent.putExtra(MONEY_KEY, money);
        startActivityForResult(intent, index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == MoneyActivity.RESULT_OK)
            deleteMoney(list.get(requestCode));
    }

    @Override
    public void onNothingSelected() { }

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
}
