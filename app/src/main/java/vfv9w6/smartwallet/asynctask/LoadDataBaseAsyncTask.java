package vfv9w6.smartwallet.asynctask;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import vfv9w6.smartwallet.MainActivity;
import vfv9w6.smartwallet.model.Money;

public class LoadDataBaseAsyncTask extends AsyncTask<Void, Void, List<Money>> {

    private WeakReference mainActivityWeakReference;
    public LoadDataBaseAsyncTask(MainActivity mainActivity){
        mainActivityWeakReference = new WeakReference(mainActivity);
    }

    @Override
    protected List<Money> doInBackground(Void... voids) {
        return Money.listAll(Money.class);
    }

    @Override
    protected void onPostExecute(List<Money> monies) {
        super.onPostExecute(monies);

        if(mainActivityWeakReference != null && monies != null)
        {
            final MainActivity mainActivity = (MainActivity) mainActivityWeakReference.get();
            if (mainActivity != null)
            {
                mainActivity.setList(monies);
                mainActivity.initChart();
            }
        }
    }
}
