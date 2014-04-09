/**
 * 
 */
package com.damaitan.mobileUI;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
	
	//private int lastHour=0;
	//private int lastMinute=0;
	private DatePicker picker=null;

	public static int getHour(String time) {
	    String[] pieces=time.split(":");
	    return(Integer.parseInt(pieces[0]));
	}

	public static int getMinute(String time) {
	    String[] pieces=time.split(":");
	    return(Integer.parseInt(pieces[1]));
	}

	public DatePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		setPositiveButtonText(ctxt.getResources().getString(R.string.ok));
		setNegativeButtonText(ctxt.getResources().getString(R.string.cancel));
	}

	@Override
	protected View onCreateDialogView() {
		picker=new DatePicker(getContext());
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        picker.init(year, monthOfYear, dayOfMonth,null);
        picker.setCalendarViewShown(false);
		return(picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		//picker.setCurrentHour(lastHour);
		//picker.setCurrentMinute(lastMinute);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			/*lastHour=picker.getCurrentHour();
			lastMinute=picker.getCurrentMinute();
			String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);
			if (callChangeListener(time)) {
				persistString(time);
			}*/
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return(a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		/*String time=null;
		if (restoreValue) {
			if (defaultValue==null) {
				time=getPersistedString("00:00");
			}
			else {
				time=getPersistedString(defaultValue.toString());
			}
		}
		else {
			time=defaultValue.toString();
		}

		lastHour=getHour(time);
		lastMinute=getMinute(time);*/
	}
}

