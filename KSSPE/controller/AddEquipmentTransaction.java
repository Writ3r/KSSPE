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
import model.Equipment;

/** The class containing the AddArticleTypeTransaction for the Professional Clothes Closet application */
//==============================================================
public class AddEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Equipment myEquipment;

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
				props.setProperty("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				props.setProperty("DateLastUsed", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				myEquipment = new Equipment(props, true);
				myEquipment.save();
				
				errorMessage = "Equipment with id: " +  myEquipment.getState("Barcode") + " added Successfully";
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

