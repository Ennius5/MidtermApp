package com.main.midterm;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UnitConverterFrag extends Fragment {

    private Spinner spinnerType, spinner1, spinner2;
    private EditText inputValue;
    private TextView resultView;
    private Button btnConvert;

    public UnitConverterFrag() { }

    public static UnitConverterFrag newInstance(String param1, String param2) {
        UnitConverterFrag fragment = new UnitConverterFrag();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_converter, container, false);

        spinnerType = view.findViewById(R.id.spinner_conversion_type);
        spinner1 = view.findViewById(R.id.spinner_original);
        spinner2 = view.findViewById(R.id.spinner_result);
        inputValue = view.findViewById(R.id.input_value);
        resultView = view.findViewById(R.id.Result);
        btnConvert = view.findViewById(R.id.btn_convert);

        // Conversion categories
        String[] categories = {"Temperature", "Weight", "Time"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spinnerType.setAdapter(typeAdapter);

        // Update unit spinners when category changes, also avoids incompatible conversions.
        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                int arrayId;
                switch (position) {
                    case 0: arrayId = R.array.temperature_units; break;
                    case 1: arrayId = R.array.weight_units; break;
                    case 2: arrayId = R.array.time_units; break;
                    default: arrayId = R.array.temperature_units; break;
                }
                ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                        requireContext(),
                        arrayId,
                        android.R.layout.simple_spinner_dropdown_item
                );
                spinner1.setAdapter(unitAdapter);
                spinner2.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // Handle conversion
        btnConvert.setOnClickListener(v -> {
            String inputStr = inputValue.getText().toString();
            if (inputStr.isEmpty()) {
                resultView.setText("Enter a value");
                return;
            }

            double input = Double.parseDouble(inputStr);
            String fromUnit = spinner1.getSelectedItem().toString();
            String toUnit = spinner2.getSelectedItem().toString();
            String category = spinnerType.getSelectedItem().toString();

            double output = 0.0;

            switch (category) {
                case "Temperature":
                    output = convertTemperature(input, fromUnit, toUnit);
                    break;
                case "Weight":
                    output = convertWeight(input, fromUnit, toUnit);
                    break;
                case "Time":
                    output = convertTime(input, fromUnit, toUnit);
                    break;
            }

            resultView.setText(String.valueOf(output));
        });

        return view;
    }

    // ---------------- Conversion Methods ----------------
    //
    private double convertTemperature(double value, String from, String to) {
        double celsius;

        // First convert to Celsius, less frustration that way
        if (from.equals("Celsius")) celsius = value;
        else if (from.equals("Fahrenheit")) celsius = (value - 32) * 5/9;
        else celsius = value - 273.15; // Kelvin

        // Convert from Celsius to target
        if (to.equals("Celsius")) return celsius;
        else if (to.equals("Fahrenheit")) return (celsius * 9/5) + 32;
        else return celsius + 273.15; // Kelvin
    }

    private double convertWeight(double value, String from, String to) {
        // Convert everything to grams first, less frustration that way
        double grams;
        switch (from) {
            case "Gram": grams = value; break;
            case "Kilogram": grams = value * 1000; break;
            case "Pound": grams = value * 453.592; break;
            case "Ounce": grams = value * 28.3495; break;
            default: grams = value; break;
        }

        switch (to) {
            case "Gram": return grams;
            case "Kilogram": return grams / 1000;
            case "Pound": return grams / 453.592;
            case "Ounce": return grams / 28.3495;
            default: return grams;
        }
    }

    private double convertTime(double value, String from, String to) {
        // Convert everything to seconds first
        double seconds;
        switch (from) {
            case "Second": seconds = value; break;
            case "Minute": seconds = value * 60; break;
            case "Hour": seconds = value * 3600; break;
            case "Day": seconds = value * 86400; break;
            default: seconds = value; break;
        }

        switch (to) {
            case "Second": return seconds;
            case "Minute": return seconds / 60;
            case "Hour": return seconds / 3600;
            case "Day": return seconds / 86400;
            default: return seconds;
        }
    }
}