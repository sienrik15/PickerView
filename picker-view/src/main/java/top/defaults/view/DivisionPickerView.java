package top.defaults.view;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

public class DivisionPickerView extends PickerViewGroup {

    private final DivisionAdapter provisionAdapter = new DivisionAdapter();
    private final DivisionAdapter cityAdapter = new DivisionAdapter();
    private final DivisionAdapter divisionAdapter = new DivisionAdapter();

    private PickerView provincePicker;
    private PickerView cityPicker;
    private PickerView divisionPicker;

    public DivisionPickerView(Context context) {
        this(context, null);
    }

    public DivisionPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivisionPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        provincePicker = new PickerView(context);
        cityPicker = new PickerView(context);
        divisionPicker = new PickerView(context);

        settlePickerView(provincePicker);
        settlePickerView(cityPicker);
        settlePickerView(divisionPicker);
    }

    public interface OnSelectedDivisionChangedListener {
        void onSelectedDivisionChanged(Division division);
    }

    private OnSelectedDivisionChangedListener onSelectedDivisionChangedListener;

    public void setOnSelectedDateChangedListener(OnSelectedDivisionChangedListener onSelectedDivisionChangedListener) {
        this.onSelectedDivisionChangedListener = onSelectedDivisionChangedListener;
    }

    public void setDivisions(List<Division> divisions) {
        provisionAdapter.setDivisions(divisions);
        provincePicker.setAdapter(provisionAdapter);

        cityAdapter.setDivisions(provisionAdapter.getItem(provincePicker.getSelectedItemPosition()).getChildren());
        cityPicker.setAdapter(cityAdapter);

        divisionAdapter.setDivisions(cityAdapter.getItem(cityPicker.getSelectedItemPosition()).getChildren());
        divisionPicker.setAdapter(divisionAdapter);

        PickerView.OnSelectedItemChangedListener listener = new PickerView.OnSelectedItemChangedListener() {
            @Override
            public void onSelectedItemChanged(PickerView pickerView, int previousPosition, int selectedItemPosition) {
                if (pickerView == provincePicker) {
                    cityAdapter.setDivisions(provisionAdapter.getItem(provincePicker.getSelectedItemPosition()).getChildren());
                    divisionAdapter.setDivisions(cityAdapter.getItem(cityPicker.getSelectedItemPosition()).getChildren());
                } else if (pickerView == cityPicker) {
                    divisionAdapter.setDivisions(cityAdapter.getItem(cityPicker.getSelectedItemPosition()).getChildren());
                }

                if (onSelectedDivisionChangedListener != null) {
                    onSelectedDivisionChangedListener.onSelectedDivisionChanged(getSelectedDivision());
                }
            }
        };

        provincePicker.setOnSelectedItemChangedListener(listener);
        cityPicker.setOnSelectedItemChangedListener(listener);
        divisionPicker.setOnSelectedItemChangedListener(listener);
    }

    public PickerView getProvincePicker() {
        return provincePicker;
    }

    public PickerView getCityPicker() {
        return cityPicker;
    }

    public PickerView getDivisionPicker() {
        return divisionPicker;
    }

    public Division getSelectedDivision() {
        Division division = divisionPicker.getSelectedItem(Division.class);
        if (division == null) {
            division = cityPicker.getSelectedItem(Division.class);
        }
        if (division == null) {
            division = provincePicker.getSelectedItem(Division.class);
        }
        return division;
    }
}