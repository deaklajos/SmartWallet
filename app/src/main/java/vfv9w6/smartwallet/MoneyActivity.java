package vfv9w6.smartwallet;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import vfv9w6.smartwallet.model.Money;

public class MoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        Intent i = getIntent();
        Money money = i.getParcelableExtra(MainActivity.MONEY_KEY);
        TextView amountTextView =  findViewById(R.id.money);

        amountTextView.setText(Integer.toString(money.money_));
        if(money.money_ > 0)
            amountTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGraphGreen, null));
        else
            amountTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGraphRed, null));

        TextView descriptionTextView =  findViewById(R.id.description);
        descriptionTextView.setText(money.description_);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TextView dateTextView =  findViewById(R.id.date);
        dateTextView.setText(df.format(money.date_));
    }
}
