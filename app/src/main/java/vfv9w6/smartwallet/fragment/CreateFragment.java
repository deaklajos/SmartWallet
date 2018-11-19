package vfv9w6.smartwallet.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import vfv9w6.smartwallet.R;
import vfv9w6.smartwallet.model.Money;

public class CreateFragment extends DialogFragment {
    private AddListener mListener;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create, container, false);
        Spinner spinner = view.findViewById(R.id.spinnerType);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.types_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        view.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO latlong
                Double lat = null;
                Double lon = null;
                EditText amountEditText = view.findViewById(R.id.amount);
                EditText descriptionEditText = view.findViewById(R.id.add_description);

                String description = descriptionEditText.getText().toString();
                String amountString = amountEditText.getText().toString();

                if (description.equals("")) {
                    descriptionEditText.setError("Description cannot be empty!");
                    return;
                }
                if (amountString.equals("")) {
                    amountEditText.setError("Amount cannot be empty!");
                    return;
                }

                int amount = Integer.parseInt(amountString);
                Money.Type type;
                int position = ((Spinner) view.findViewById(R.id.spinnerType)).getSelectedItemPosition();
                switch (position) {
                    case 0:
                        type = Money.Type.SALARY;
                        break;
                    case 1:
                        type = Money.Type.POCKET_MONEY;
                        break;
                    case 2:
                        type = Money.Type.OTHER;
                        break;
                    case 3:
                        type = Money.Type.GROCERIES;
                        break;
                    default:
                        type = Money.Type.OTHER;
                        break;
                }
                Money money = new Money(amount, type, description, new Date(), lat, lon);
                mListener.onAdd(money);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddListener) {
            mListener = (AddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AddListener {
        void onAdd(Money money);
    }
}
