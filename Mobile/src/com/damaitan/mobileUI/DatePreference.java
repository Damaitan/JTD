/**
 * 
 */
package com.damaitan.mobileUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.damaitan.datamodel.Task;
import com.damaitan.presentation.TaskListSorter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

/**
 * @author admin
 *
 */
@SuppressLint("NewApi")
public class DatePreference extends DialogPreference implements DatePicker.OnDateChangedListener{
	private DatePicker picker=null;
	private Date date;


	public DatePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		setPositiveButtonText(ctxt.getResources().getString(R.string.ok));
		setNegativeButtonText(ctxt.getResources().getString(R.string.cancel));
	}
	
	@Override
	protected View onCreateDialogView() {
		picker=new DatePicker(getContext());
		if(date != null){
			picker.init(date.getYear(), date.getMinutes(), date.getDay(), this);
		}else{
			Calendar calendar=Calendar.getInstance(Locale.CHINA);
			picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, Calendar.DAY_OF_MONTH,this);
			//picker.init(date.getYear(), date.getMinutes(), Calendar.DAY_OF_MONTH,this);
		}
        picker.setCalendarViewShown(false);
        return picker;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			int month = picker.getMonth() + 1;
			callChangeListener(picker.getYear() + Task.DATESPLITTER + month + Task.DATESPLITTER  + picker.getDayOfMonth());
		}
		super.onDialogClosed(positiveResult);
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null) {
			String value = (String)defaultValue;
			if(value == null || value.trim().isEmpty()){
				super.setDefaultValue(defaultValue);
				return;
			}
			SimpleDateFormat df = TaskListSorter.dateFormat();
			try {
				date = df.parse(value);
			} catch (ParseException e) {
				super.setDefaultValue(defaultValue);
				return;
			}
			
		}
			
		super.setDefaultValue(defaultValue);
	}

	@Override
	public void onDateChanged(DatePicker arg0, int year, int month, int dayOfMonth) {
		// TODO Auto-generated method stub
		
	}
	
	
}

