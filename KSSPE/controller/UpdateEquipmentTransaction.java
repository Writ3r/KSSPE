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

/** The class containing the ModifyCategoryTransaction for the KSSPE application */
//==============================================================
public class UpdateEquipmentTransaction extends Transaction
{
	private String errorMessage = "";
	private Receptionist myReceptionist;
	private Equipment myEquipment;
	private EquipmentCollection myEquipmentList;

	//----------------------------------------------------------------
	public UpdateEquipmentTransaction() throws Exception
	{
		super();
	}

	//----------------------------------------------------------------
	public void processTransaction(Properties props)
	{
		myEquipmentList = new EquipmentCollection();
		
		if (props.getProperty("Barcode") != null)
		{
			String barcode = props.getProperty("Barcode");
			myEquipmentList.findByBarcodePrefix(barcode);
		}
		else if(props.getProperty("Name") != null)
		{
			String name = props.getProperty("Name");
			myEquipmentList.findByName(name);
		}
		else
		{
			myEquipmentList.findAll();
		}
		
		
		try
		{	
			Scene newScene = createEquipmentCollectionView();	
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
		else if (key.equals("EquipmentList") == true)
		{
			return myEquipmentList;
		}
		else if (key.equals("Barcode") == true)
		{
			return myEquipment.getState("Barcode");
		}
		else if (key.equals("Name") == true)
		{
			return myEquipment.getState("Name");
		}
		else if (key.equals("CategoryName") == true)
		{
			return myEquipment.getState("CategoryName");
		}
		else if (key.equals("Notes") == true)
		{
			return myEquipment.getState("Notes");
		}
		else if (key.equals("PoorCount") == true)
		{
			return myEquipment.getState("PoorCount");
		}
		else if (key.equals("FairCount") == true)
		{
			return myEquipment.getState("FairCount");
		}
		else if (key.equals("GoodCount") == true)
		{
			return myEquipment.getState("GoodCount");
		}
		else if (key.equals("AvailableCount") == true)
		{
			return myEquipment.getState("AvailableCount");
		}
		else if (key.equals("InStockCount") == true)
		{
			return myEquipment.getState("InStockCount");
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
			doYourJob();
		}
		if (key.equals("SearchEquipment") == true)
		{
			processTransaction((Properties)value);
		}
		if (key.equals("EquipmentSelected") == true)
		{
			myEquipment = myEquipmentList.retrieve((String)value);
			try
			{
				Scene newScene = createModifyEquipmentView();
				swapToView(newScene);
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Error in creating ModifyEquipmentView", Event.ERROR);
			}
		}
		if (key.equals("EquipmentData") == true)
		{
			modifyEquipmentHelper((Properties)value);
		}
		if (key.equals("CancelEquipmentList") == true)
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
	private void modifyEquipmentHelper(Properties props)
	{
		
		myEquipment.stateChangeRequest("Name", props.getProperty("Name"));
		myEquipment.stateChangeRequest("CategoryName", props.getProperty("CategoryName"));
		myEquipment.stateChangeRequest("Notes", props.getProperty("Notes"));
		myEquipment.stateChangeRequest("PoorCount", props.getProperty("PoorCount"));
		myEquipment.stateChangeRequest("FairCount", props.getProperty("FairCount"));
		myEquipment.stateChangeRequest("GoodCount", props.getProperty("GoodCount"));

		int oldAvail = Integer.parseInt((String)myEquipment.getState("AvailableCount"));
		int newAvail = Integer.parseInt(props.getProperty("GoodCount")) + Integer.parseInt(props.getProperty("FairCount")) + Integer.parseInt(props.getProperty("PoorCount"));
		int oldInStock = Integer.parseInt((String)myEquipment.getState("InStockCount"));
		int newInStock = oldInStock - (oldAvail - newAvail);


		if(newAvail == 0 && oldAvail != oldInStock)
		{
			errorMessage = "ERROR: You cannot remove all items before they are in stock.";
			return;
		}
		if(newInStock < 0)
		{
			errorMessage = "ERROR: In Stock Count would be less than zero.";
			return;
		}


		myEquipment.stateChangeRequest("AvailableCount", Integer.toString(newAvail));
		myEquipment.stateChangeRequest("InStockCount", Integer.toString(newInStock));
		myEquipment.stateChangeRequest("DateAdded", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));


		myEquipment.save();
		errorMessage = (String)myEquipment.getState("UpdateStatusMessage");
	}
	
	//-----------------------------------------------------------------------
	protected Scene createModifyEquipmentView()
	{
		Scene currentScene = myViews.get("UpdateEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("UpdateEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("UpdateEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}
	
	//--------------------------------------------------------------------------
	protected Scene createEquipmentCollectionView()
	{
		Scene currentScene;
		View newView = ViewFactory.createView("EquipmentCollectionView", this);
		currentScene = new Scene(newView);

		return currentScene;
	}

	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("SearchEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("SearchEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}
}

