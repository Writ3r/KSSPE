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
import model.CheckOut;
import model.CheckOutCollection;

/** The class containing the ListOverDueInventoryTransaction for the KSSPE application */
//==============================================================
public class ListOverdueInventoryTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private CheckOut myCheckOut;
	private CheckOutCollection myCheckOutList;


	//------------------------------------------------------------
	public ListOverdueInventoryTransaction() throws Exception
	{
		super();
		processTransaction();
	}

	//------------------------------------------------------------
	public void processTransaction()
	{
		myCheckOutList = new CheckOutCollection();
		myCheckOutList.findAllOverdue();
		
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
		else if (key.equals("CheckOutList") == true)
		{
			return myCheckOutList;
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
			myCheckOut = null;
		}
		else if (key.equals("CancelCheckOutList") == true)
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

		View newView = ViewFactory.createView("CheckOutCollectionReportView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}
}

