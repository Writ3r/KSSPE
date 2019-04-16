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
import model.Borrower;
import model.Equipment;
import model.CheckOut;
import model.BorrowerCollection;

/** The class containing the ModifyBorrowerTransaction for the KSSPE application */
//==============================================================
public class ReserveEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Borrower myBorrower;
	private Equipment myCurrentEquipment;
	private BorrowerCollection myBorrowerList;
	private String myWorkerId;

	//----------------------------------------------------------------
	public ReserveEquipmentTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myBorrowerList = new BorrowerCollection();
		
		if (props.getProperty("BannerId") != null)
		{
			String bannerId = props.getProperty("BannerId");
			myBorrowerList.findByBannerId(bannerId);
		}
		else if(props.getProperty("FirstName") != null || props.getProperty("LastName") != null)
		{
			if(props.getProperty("FirstName") != null && props.getProperty("LastName") != null)
			{
				myBorrowerList.findByFirstAndLast(props);
			}
			else if(props.getProperty("FirstName") != null)
			{
				String name = props.getProperty("FirstName");
				myBorrowerList.findByFirstName(name);
			}
			else
			{
				String name = props.getProperty("LastName");
				myBorrowerList.findByLastName(name);
			}
		}
		else if (props.getProperty("PhoneNumber") != null)
		{
			String phone = props.getProperty("PhoneNumber");
			myBorrowerList.findByPhone(phone);
		}
		else
		{
			myBorrowerList.findAll();
		}
		
		try
		{	
			Scene newScene = createBorrowerCollectionView();	
			swapToView(newScene);
		}
		catch (Exception ex)
		{
			new Event(Event.getLeafLevelClassName(this), "processTransaction",
					"Error in creating BorrowerCollectionView", Event.ERROR);
		}
	}
	
	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("Error") == true)
		{
			return errorMessage;
		}
		else if (key.equals("BorrowerList") == true)
		{
			return myBorrowerList;
		}
		else if (key.equals("BorrowerBannerId") == true)
		{
			return myBorrower.getState("BannerId");
		}
		else if (key.equals("WorkerBannerId") == true)
		{
			return myWorkerId;
		}
		else if (key.equals("Penalty") == true)
		{
			return myBorrower.getState("Penalty");
		}
		else if (key.equals("BlockStatus") == true)
		{
			return myBorrower.getState("BlockStatus");
		}
		else if (key.equals("Notes") == true)
		{
			return myBorrower.getState("Notes");
		}
		else if (key.equals("TestEquipment") == true)
		{
			if(myCurrentEquipment != null)
				return true;
			else
				return false;
		}
		else
			return null;
	}

	//-------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		errorMessage = "";
		
		if (key.equals("DoYourJob") == true)
		{
			myReceptionist = (Receptionist)value;
			myWorkerId = (String)myReceptionist.getState("BannerId");
			doYourJob();
		}
		if (key.equals("SearchBorrower") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("BorrowerSelected") == true)
		{
			myBorrower = myBorrowerList.retrieve((String)value);
			
			try
			{
				Scene newScene = createReserveEquipmentView();
				swapToView(newScene);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Error in creating ModifyBorrowerView", Event.ERROR);
			}
		}
		if (key.equals("CheckOutData") == true)
		{
			reserveEquipment((Properties)value);
		}
		if (key.equals("TestEquipment") == true)
		{
			myCurrentEquipment = null; //clears out past equipment.
			
			try
			{
				myCurrentEquipment = new Equipment((Properties)value);
				
				errorMessage = "Equipment with Barcode: " + ((Properties)value).getProperty("Barcode") +  " found!";
			}
			catch(Exception ex)
			{
				errorMessage = "ERROR: Equipment with Barcode: " + ((Properties)value).getProperty("Barcode") +  " does not Exist!";
			}
		}
		if (key.equals("CancelBorrowerList") == true)
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
	
	//------------------------------------------------------------------------
	private void reserveEquipment(Properties props)
	{
		int stock = Integer.parseInt((String)myCurrentEquipment.getState("InStockCount"));
		int taken = Integer.parseInt(props.getProperty("UnitsTaken"));
		
		if(stock - taken >= 0)
		{
			props.setProperty("BannerId", (String)getState("BorrowerBannerId"));
			props.setProperty("TotalUnitsReturned", "0");
			props.setProperty("RentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			props.setProperty("CheckOutWorkerID", myWorkerId);
			
			CheckOut checkOut = new CheckOut(props);
			checkOut.save();
			
			myCurrentEquipment.stateChangeRequest("InStockCount", Integer.toString(stock - taken));
			myCurrentEquipment.save();
			
			errorMessage = (String)checkOut.getState("UpdateStatusMessage");
		}
		else
			errorMessage = "ERROR: Cannot exceed the " + stock + " units in stock";
		
	}
	
	//-----------------------------------------------------------------------
	protected Scene createReserveEquipmentView()
	{
		Scene currentScene = myViews.get("ReserveEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("ReserveEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("ReserveEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}
	
	//--------------------------------------------------------------------------
	protected Scene createBorrowerCollectionView()
	{
		Scene currentScene;

		View newView = ViewFactory.createView("BorrowerCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchBorrowerReserveView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchBorrowerReserveView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchBorrowerReserveView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

