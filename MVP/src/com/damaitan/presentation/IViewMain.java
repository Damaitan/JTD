/**
 * 
 */
package com.damaitan.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.damaitan.datamodel.TaskFolder;

/**
 * @author admin
 *
 */
public interface IViewMain {
	List<Map<String, Object>> listItems(ArrayList<TaskFolder> folders);
}
