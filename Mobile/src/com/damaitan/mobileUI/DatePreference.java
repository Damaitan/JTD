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
public class DatePreference extends DialogPreference {
	private DatePicker picker=null;

	private int year = -1;
	private int month = -1 ;
	private int day = -1;

	public DatePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		setPositiveButtonText(ctxt.getResources().getString(R.string.ok));
		setNegativeButtonText(ctxt.getResources().getString(R.string.cancel));
	}
	
	@Override
	protected View onCreateDialogView() {
		picker=new DatePicker(getContext());
		Calendar calendar=Calendar.getInstance(Locale.CHINA);
		if(day != -1){
			picker.init(year, month, day,null);
		}else{
			picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Calendar.DAY_OF_MONTH,null);
		}
        picker.setCalendarViewShown(false);
        return picker;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.CHINESE);
			df.getCalendar().set(Calendar.YEAR, picker.getYear());
			df.getCalendar().set(Calendar.MONTH, picker.getMonth());
			df.getCalendar().set(Calendar.MONDAY, picker.getDayOfMonth());
			df.getCalendar().toString();
			callChangeListener(df.getCalendar().toString());
		}
		super.onDialogClosed(positiveResult);
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null) {
			String value = (String)defaultValue;
			if(value == null || value.trim().equalsIgnoreCase("")){
				super.setDefaultValue(defaultValue);
				return;
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy" + Task.DATESPLITTER + "MM" + Task.DATESPLITTER + "dd",  Locale.CHINESE);
			try {
				df.parse(value);
			} catch (ParseException e) {
				super.setDefaultValue(defaultValue);
				return;
			}
			picker.updateDate(df.getCalendar().get(Calendar.YEAR), df.getCalendar().get(Calendar.MONTH), df.getCalendar().get(Calendar.MONDAY));
		}
			
		super.setDefaultValue(defaultValue);
	}
	
	
}

