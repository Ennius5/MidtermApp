package com.main.midterm;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class BaseNumCalc extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // UI elements
    private TextInputEditText inputNumber;
    private TextInputLayout inputLayout;
    private Spinner spinnerOriginal, spinnerResult;
    private TextView resultText, allresultText;
    private Button convertButton;

    // Base options
    private String[] bases = {"Binary (2)", "Octal (8)", "Decimal (10)", "Hexadecimal (16)"};
    private final int[] baseValues = {2, 8, 10, 16};
    private final String[] baseValueNames = {"Binary", "Octal", "Decimal", "Hexadecimal"};

    public BaseNumCalc() {
        // Required empty public constructor
    }

    public static BaseNumCalc newInstance(String param1, String param2) {
        BaseNumCalc fragment = new BaseNumCalc();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_num_calc, container, false);

        initializeViews(view);
        setupSpinners();
        setupConvertButton();

        return view;
    }

    private void initializeViews(View view) {
        inputNumber = view.findViewById(R.id.inputNumber);
        inputLayout = view.findViewById(R.id.textInputLayout);
        spinnerOriginal = view.findViewById(R.id.spinner_original);
        spinnerResult = view.findViewById(R.id.spinner_result);
        resultText = view.findViewById(R.id.Result);
        allresultText = view.findViewById(R.id.otherResults);
        convertButton = view.findViewById(R.id.convertButton);
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                bases
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOriginal.setAdapter(adapter);
        spinnerResult.setAdapter(adapter);

        // Set default selections
        spinnerOriginal.setSelection(2); // Decimal
        spinnerResult.setSelection(0);   // Binary
    }

    private void setupConvertButton() {
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertNumber();
            }
        });
    }

    private void convertNumber() {
        String input = inputNumber.getText().toString().trim().toUpperCase();

        if (input.isEmpty()) {
            showError("Please enter a number");
            return;
        }

        clearError();

        try {
            int fromBase = baseValues[spinnerOriginal.getSelectedItemPosition()];
            int toBase = baseValues[spinnerResult.getSelectedItemPosition()];

            String result = convertBase(input, fromBase, toBase);
            String totalResult = converttoOthers(input,fromBase);

            resultText.setText(result);
            allresultText.setText(totalResult);
        } catch (NumberFormatException e) {
            showError("Invalid number for selected base");
        } catch (Exception e) {
            showError("Conversion error: " + e.getMessage());
        }
    }

    private String convertBase(String number, int fromBase, int toBase) {
        // First convert to decimal...
        long decimalValue;
        if (fromBase == toBase){
            return number; //why bother converting to the same base lol
        }
        if (fromBase == 10) {
            decimalValue = Long.parseLong(number);
        } else {
            decimalValue = Long.parseLong(number, fromBase);
        }

        // Then convert from decimal to target base
        if (toBase == 10) {
            return String.valueOf(decimalValue);
        } else {
            return Long.toString(decimalValue, toBase).toUpperCase();
        }


    }

    /**
     * No matter what base, gets converted anyway
     * */
    private String converttoOthers(String number, int fromBase){
        StringBuilder totality = new StringBuilder();
        long decimalValue;
        if (fromBase == 10) {
            decimalValue = Long.parseLong(number);
        } else {
            decimalValue = Long.parseLong(number, fromBase);
        }

        //all the values are converted then the kak is fed into the totality
        int i = 0;
        for(int toBase : baseValues){

            totality.append( baseValueNames[i] +" : "+Long.toString(decimalValue,toBase).toUpperCase()+"\n");
            i++;
        }
        return totality.toString();
    }
    private void showError(String errorMessage) {
        inputLayout.setError(errorMessage);
        resultText.setText("ERROR");
        allresultText.setText("ERROR");

    }

    private void clearError() {
        inputLayout.setError(null);

    }


}