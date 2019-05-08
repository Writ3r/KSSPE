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

/** The class containing the AddArticleTypeTransaction for the Professional Clothes Closet application */
//==============================================================
public class AddEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Equipment myEquipment;
	private Category selectedCategory;

	//----------------------------------------------------------------------------
	public AddEquipmentTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		try
		{
			Equipment e = new Equipment(props);
			
			errorMessage = "ERROR: Equipment with id: " + e.getState("Barcode") + " already exists!";	
		}
		catch (InvalidPrimaryKeyException ex) 
		{
			
			try
			{
				String poorCount = props.getProperty("PoorCount");
				String goodCount = props.getProperty("GoodCount");
				String fairCount = props.getProperty("FairCount");
				
				int availableCount = Integer.parseInt(poorCount) + Integer.parseInt(fairCount) + Integer.parseInt(goodCount);
				int inStockCount = availableCount;
				props.setProperty("AvailableCount", Integer.toString(availableCount));
				props.setProperty("InStockCount", Integer.toString(inStockCount));
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUsed", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myEquipment = new Equipment(props, true);
				myEquipment.save();
				
				errorMessage = "Equipment with id: " +  myEquipment.getState("Barcode") + " added Successfully";
			}
			catch (InvalidPrimaryKeyException ex2) 
			{
				errorMessage = "ERROR: " + ex2.getMessage();
			}
			catch (Exception ex3)
			{
				errorMessage = "ERROR: " + ex3.getMessage();
			}
			
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("CategoryName") == true)
		{
			if (selectedCategory != null)
			{
				return selectedCategory.getState("Name");
			}
			else
			{
				return "None";
			}
		}
		else
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else
			return null;
	}

	//------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("AddArticleTypeTransaction.sCR: key: " + key);
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		if (key.equals("Barcode") == true)
		{
			String sentBarcode = (String)value;
			String barcodePrefx = sentBarcode.substring(0, 3);
			Properties p = new Properties();
			p.setProperty("BarcodePrefix", barcodePrefx);
			try
			{
				selectedCategory = new Category(p);
				errorMessage = "CategorySelected";
				
			}
			catch (Exception ex)
			{
				selectedCategory = null;
				errorMessage = "CategorySelected";
			}
		}
		if (key.equals("EquipmentData") == true)
		{
			processTransaction((Properties)value);
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
		Scene currentScene = myViews.get("AddEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("AddEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("AddEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

