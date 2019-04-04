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
import model.Equipment;
import model.CategoryCollection;
import model.EquipmentCollection;

/** The class containing the RemoveCategoryTransaction for the KSSPE application */
//==============================================================
public class RemoveCategoryTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Category myCategory;
	private CategoryCollection myCategoryList;

	//--------------------------------------------------------------------------
	public RemoveCategoryTransaction() throws Exception
	{
		super();
	}

	//--------------------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myCategoryList = new CategoryCollection();
		
		if (props.getProperty("BarcodePrefix") != null)
		{
			String barcodePrefix = props.getProperty("BarcodePrefix");
			myCategoryList.findByBarcodePrefix(barcodePrefix);
		}
		else if(props.getProperty("Name") != null)
		{
			String name = props.getProperty("Name");
			myCategoryList.findByName(name);
		}
		else
		{
			myCategoryList.findAll();
		}
		
		
		try
		{	
			Scene newScene = createCategoryCollectionView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating ArticleTypeCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("CategoryList") == true)
		{
			return myCategoryList;
		}
		else if (key.equals("BarcodePrefix") == true)
		{
			return myCategory.getState("BarcodePrefix");
		}
		else if (key.equals("Name") == true)
		{
			return myCategory.getState("Name");
		}
		else if (key.equals("Alert") == true)
		{
			return true;
		}
		
		else
			return null;
	}

	//--------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("SearchCategory") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("CategorySelected") == true)
		{
			myCategory = myCategoryList.retrieve((String)value);
			
			removeCategoryHelper();
		}
		if (key.equals("CancelCategoryList") == true)
		{
			Scene oldScene = createView();	
			swapToView(oldScene);
		}
		if (key.equals("CancelTransaction") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}
	
	
	//----------------------------------------------------------------------
	private void removeCategoryHelper()
	{
		String catName = (String)myCategory.getState("Name");
		EquipmentCollection ec = new EquipmentCollection();
		ec.findByCategoryName(catName);
		Vector<Equipment> v = (Vector)ec.getState("AllEquipment");
		if ((v != null) && (v.size() > 0))
		{
			errorMessage = "ERROR: Category non-removable: EQUIPMENT EXISTS!";
			return;
		}
		
		
		myCategory.stateChangeRequest("Status", "Inactive");
		myCategory.save();
		
		errorMessage = (String)myCategory.getState("UpdateStatusMessage");
		
		if(errorMessage.startsWith("ERR") == false)
		{
			if (myCategoryList != null)
			{
				myCategoryList.remove((String)myCategory.getState("BarcodePrefix"));
			}
			errorMessage = errorMessage.replace("updated", "removed");
		}
	}
	
	//-----------------------------------------------------------------------
	protected Scene createCategoryCollectionView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("CategoryCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchCategoryView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchCategoryView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchCategoryView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

