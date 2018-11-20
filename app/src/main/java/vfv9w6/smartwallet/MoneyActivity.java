package vfv9w6.smartwallet;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import vfv9w6.smartwallet.model.Money;

public class MoneyActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int RESULT_OK = 1;
    private GoogleMap mMap;
    private LatLng latLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Money money = i.getParcelableExtra(MainActivity.MONEY_KEY);
        TextView amountTextView = findViewById(R.id.money);

        amountTextView.setText(Integer.toString(money.money));
        if (money.money > 0)
            amountTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGraphGreen, null));
        else
            amountTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGraphRed, null));

        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(money.description);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TextView dateTextView = findViewById(R.id.date);
        dateTextView.setText(df.format(money.date));

        ImageView imageView = findViewById(R.id.image);
        switch (money.type) {
            case GROCERIES:
                imageView.setImageResource(R.drawable.groceries_cropped_resized);
                break;
            case SALARY:
                imageView.setImageResource(R.drawable.salary_cropped_resized);
                break;
            case POCKET_MONEY:
                imageView.setImageResource(R.drawable.pocket_money_cropped_resized);
                break;
            case OTHER:
                imageView.setImageResource(R.drawable.other_cropped_resized);
                break;
        }

        if(money.latitude != null && money.longitude != null)
            latLng = new LatLng(money.latitude, money.longitude);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        if (id == R.id.action_delete) {

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(latLng != null)
        {
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }
}
