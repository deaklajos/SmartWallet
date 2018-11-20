package vfv9w6.smartwallet.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import vfv9w6.smartwallet.R;
import vfv9w6.smartwallet.model.Money;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends DialogFragment {
    private AddListener mListener;


    private static int PLACE_PICKER_REQUEST = 1;
    private Double lat = null;
    private Double lon = null;

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

        view.findViewById(R.id.btnAddLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), R.string.googleERROR, Toast.LENGTH_LONG).show();

                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), R.string.googleERROR, Toast.LENGTH_LONG).show();
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                LatLng latLng = PlacePicker.getPlace(getActivity(), data).getLatLng();
                lat = latLng.latitude;
                lon = latLng.longitude;
            }
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
