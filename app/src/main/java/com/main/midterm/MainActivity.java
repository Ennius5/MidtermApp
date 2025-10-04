package com.main.midterm;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment frag_View;
    private Spinner spinnerDropdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerDropdown = findViewById(R.id.spinnermain);
        spinnerDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Functionality","Now selected:" +spinnerDropdown.getSelectedItem().toString());
                String a = spinnerDropdown.getSelectedItem().toString();
                switch (a){
                    case "Basic Calculator":
                        Log.v("Func_inner","basic calculator switch!");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container, new CalcFrag())
                                .commit();
                        return;
                    case "Base Number Converter":
                        Log.v("Func_inner","basic number converter switch!");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container, new BaseNumCalc())
                                .commit();
                        return;

                    case "Unit Converter":
                        Log.v("Func_inner","basic unit converter switch!");
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_container, new UnitConverterFrag())
                                .commit();
                        return;

                    default:
                        Log.v("ERR","Somehow there's an error");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.selection,
                android.R.layout.simple_spinner_dropdown_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDropdown.setAdapter(adapter);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_container, new CalcFrag())
                    .commit();

        }
    }
}