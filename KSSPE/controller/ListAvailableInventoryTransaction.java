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
import model.EquipmentCollection;

/** The class containing the ListAllInventoryTransaction for the KSSPE application */
//==============================================================
public class ListAvailableInventoryTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Equipment myEquipment;
	private EquipmentCollection myEquipmentList;


	//------------------------------------------------------------
	public ListAvailableInventoryTransaction () throws Exception
	{
		super();
		processTransaction();
	}

	//------------------------------------------------------------
	public void processTransaction()
	{
		myEquipmentList = new EquipmentCollection();
		myEquipmentList.findAll();
		
		try
		{	
			Scene newScene = createView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating EquipmentCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("Alert") == true)
		{
			return true;
		}
		else if (key.equals("EquipmentList") == true)
		{
			return myEquipmentList;
		}
		else
			return null;
	}

	//-----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			doYourJob();
		}
		else if(key.equals("clearState") == true)
		{
			myEquipment = null;
		}
		else if (key.equals("CancelEquipmentList") == true)
		{
			myReceptionist.stateChangeRequest("CancelTransaction", null);
		}
		
		setChanged();
        notifyObservers(errorMessage);
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("EquipmentCollectionReportView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}
}

