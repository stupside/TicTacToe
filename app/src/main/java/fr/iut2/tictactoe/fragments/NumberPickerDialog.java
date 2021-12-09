package fr.iut2.tictactoe.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Consumer;

import fr.iut2.tictactoe.R;

public class NumberPickerDialog extends DialogFragment {

    private final Integer min;
    private final Integer max;
    private Integer value;
    private Consumer<Integer> onChange;

    public NumberPickerDialog(Integer min, Integer max, Integer def) {
        value = def;
        this.min = min;
        this.max = max;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        FragmentActivity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.dialog_grid_size);

        NumberPicker picker = new NumberPicker(activity);
        picker.setMinValue(min);
        picker.setMaxValue(max);

        LinearLayout layout = new LinearLayout(activity);
        layout.addView(picker);

        builder.setView(layout);

        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            value = picker.getValue();
            callOnChange(getValue());
        });

        return builder.create();
    }

    public Integer getValue() {
        return value;
    }

    public void setOnChange(Consumer<Integer> consumer) {
        onChange = consumer;
    }

    public void callOnChange() {
        onChange.accept(getValue());
    }

    public void callOnChange(Integer integer) {
        onChange.accept(integer);
    }
}
