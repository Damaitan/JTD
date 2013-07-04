package com.damaitan.access;

import java.util.ArrayList;
import com.damaitan.datamodel.TaskFolder;
import com.damaitan.exception.AccessException;


public interface IDataAccess{
	ArrayList<TaskFolder> construct() throws AccessException;
	System getSystem();
}
