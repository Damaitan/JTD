/**
 * 
 */
package com.damaitan.mobileUI;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author admin
 *
 */
public class TagPreference extends DialogPreference {

	public TagPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(context.getResources().getString(R.string.ok));
		setNegativeButtonText(context.getResources().getString(R.string.cancel));
	}

	/* (non-Javadoc)
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@Override
	protected View onCreateDialogView() {
		//this.setDialogLayoutResource(R.layout.tag_perference);
		return super.onCreateDialogView();
	}
	
	

}
