package vfv9w6.smartwallet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import vfv9w6.smartwallet.fragment.CreateFragment;
import vfv9w6.smartwallet.model.Money;

public class MainActivity extends AppCompatActivity implements CreateFragment.AddListener {

    private BarChart chart;
    private int greenColor;
    private int redColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greenColor = ResourcesCompat.getColor(getResources(), R.color.colorGraphGreen, null);
        redColor = ResourcesCompat.getColor(getResources(), R.color.colorGraphRed, null);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setTitle("BarChartPositiveNegative");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                CreateFragment fragment = new CreateFragment();
                fragment.show(getFragmentManager(), "Create");
                //Intent intent = new Intent(getBaseContext(), MoneyActivity.class);
                //getBaseContext().startActivity(intent);
            }
        });


        InitChart();
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

    private void InitChart() {
        // Chart contents
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
        // TODO remove this
        Money.deleteAll(Money.class);

        if(Money.count(Money.class, null, null) == 0)
        {
            //TODO do date
            Date today = new Date();
            today = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 30));

            Random rnd = new Random();
            for(int i = 0; i < 30; i++)
            {
                Money pocket_money = new Money(5000 + rnd.nextInt(1000),
                        Money.Type.POCKET_MONEY, "Money from home",
                        today = new Date(today.getTime() + (1000 * 60 * 60 * 24)),
                        47.0,19.0);

                pocket_money.save();

                for(int j = 0; j < 3; j++)
                {
                    Money spend = new Money(-1000 - rnd.nextInt(1000),
                            Money.Type.GROCERIES, "Food",
                            today = new Date(today.getTime() + (1000 * 60 * 60 * 24)),
                            47.0,19.0);

                    spend.save();
                }
            }
        }

        List<Money> list = Money.listAll(Money.class);
        final List<Data> data = new ArrayList<>();
        float i = 0f;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Money money: list)
        {
            data.add(new Data(i++, money.money_, df.format(money.date_)));
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

        BarEntry entry = new BarEntry(chart.getBarData().getEntryCount(), money.money_);
        BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(0);
        set.addEntry(entry);
        if (money.money_ >= 0)
            set.addColor(greenColor);
        else
            set.addColor(redColor);
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
    }

    @Override
    public void onAdd(Money money) {
        addMoney(money);
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
}
