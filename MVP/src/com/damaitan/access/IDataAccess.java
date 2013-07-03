package com.damaitan.access;

import java.util.ArrayList;
import com.damaitan.datamodel.TaskFolder;


public interface IDataAccess{
	ArrayList<TaskFolder> construct();
	System getSystem();
}
