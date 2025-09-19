package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int position, String name, String province);
    }

    private AddCityDialogListener listener;

    public static AddCityFragment newInstance(int position, City city) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putInt("pos", position);
        b.putSerializable("city", city);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        int pos = -1;
        if (args != null) {
            pos = args.getInt("pos", -1);
            City c = (City) args.getSerializable("city");
            if (c != null) {
                editCityName.setText(c.getName());
                editProvinceName.setText(c.getProvince());
            }
        }

        int finalPos = pos;
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add/edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String name = editCityName.getText().toString();
                    String prov = editProvinceName.getText().toString();
                    if (finalPos >= 0) {
                        listener.editCity(finalPos, name, prov);
                    } else {
                        listener.addCity(new City(name, prov));
                    }
                })
                .create();
    }
}
