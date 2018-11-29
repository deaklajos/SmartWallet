package vfv9w6.smartwallet.asynctask;

import android.os.AsyncTask;

import vfv9w6.smartwallet.model.Money;

public class DeleteMoneyAsyncTask extends AsyncTask<Money, Void, Void> {
    @Override
    protected Void doInBackground(Money... monies) {
        if(monies != null)
            for (Money money: monies) {
                if (money != null)
                    money.delete();
            }
         return null;
    }
}
