/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.Calendar;

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

	

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@Override
	protected View onCreateDialogView() {
		picker=new DatePicker(getContext());
		Calendar calendar=Calendar.getInstance();
		if(day != -1){
			picker.init(year, month, day,null);
		}else{
			picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Calendar.DAY_OF_MONTH,null);
		}
        picker.setCalendarViewShown(false);
		//return super.onCreateDialogView();
        return picker;
	}
	
	
	
	

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			String time=String.valueOf(picker.getYear())+Task.DATESPLITTER+String.valueOf(picker.getMonth()) + Task.DATESPLITTER + String.valueOf(picker.getDayOfMonth());
			callChangeListener(time);
		}
		super.onDialogClosed(positiveResult);
	}

	/* (non-Javadoc)
	 * @see android.preference.Preference#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(Object defaultValue) {
		if (defaultValue != null) {
			String time = (String)defaultValue;
			String[] pieces = time.split(Task.DATESPLITTER);
			year = (Integer.parseInt(pieces[0]));
			month = (Integer.parseInt(pieces[1]));
			day = (Integer.parseInt(pieces[2]));
			picker.updateDate(year, month, day);
		}
		super.setDefaultValue(defaultValue);
	}
	
	
}

