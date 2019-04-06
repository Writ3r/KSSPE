// specify the package
package controller;

// system imports
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Date;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.MultiplePrimaryKeysException;

import userinterface.View;
import userinterface.ViewFactory;
import model.Category;

/** The class containing the AddCategoryTransaction for the KSSPE application */
//==============================================================
public class AddCategoryTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Category myCategory;

	public AddCategoryTransaction() throws Exception
	{
		super();
	}

	public void processTransaction(Properties props)
	{
		try
		{
			myCategory = new Category(props);	
			
			if(myCategory.getState("Status").equals("Active")) //if a category already exists and is active
			{
				errorMessage = "ERROR: Category already exists!";
			}
			else //update the category with the default information and set it to active again.
			{
				myCategory.stateChangeRequest("Status", "Active");
				myCategory.stateChangeRequest("Name", props.getProperty("Name"));
				myCategory.save();
				
				errorMessage = "Category with prefix: " + myCategory.getState("BarcodePrefix") + " reinstated Successfully";
			}	
		}
		catch (InvalidPrimaryKeyException ex) 
		{
			try
			{
				props.setProperty("Status", "Active");
				
				myCategory = new Category(props, true);
				myCategory.save();
				
				errorMessage = "Category Added Successfully";
			}
			catch (InvalidPrimaryKeyException ex2) 
			{
				errorMessage = ex2.getMessage();
			}
			
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("CategoryIsInactive") == true)
		{
			if(myCategory != null)
			{
				if(myCategory.getState("Status").equals("Inactive"))
					return myCategory.getState("Name");
				else
					return null;
			}
			else
				return null;
		}
		else
			return null;
	}

	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("CategoryData") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("TestCategory") == true)
		{
			try
			{
				myCategory = new Category((Properties)value);	
			}
			catch (InvalidPrimaryKeyException ex) 
			{
				//do nothing, I don't care.
			}
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("AddCategoryView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("AddCategoryView", this);
			currentScene = new Scene(newView);
			myViews.put("AddCategoryView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

