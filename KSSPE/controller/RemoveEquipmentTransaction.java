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

/** The class containing the RemoveEquipmentTransaction for the KSSPE application */
//==============================================================
public class RemoveEquipmentTransaction extends UpdateEquipmentTransaction
{
	

	//----------------------------------------------------------------
	public RemoveEquipmentTransaction() throws Exception
	{
		super();
	}

	//------------------------------------------------------------------------
	protected void modifyEquipmentHelper(Properties props)
	{
		// DEBUG System.out.println("Coming here");
		super.modifyEquipmentHelper(props);
		if (errorMessage.startsWith("ERROR") == false)
		{
			// DEBUG System.out.println("Replacing update with remove");
			errorMessage = errorMessage.replaceAll("update", "remove");
		}
		// DEBUG System.out.println(errorMessage);
	}
	
	
	//-----------------------------------------------------------------------
	protected Scene createModifyEquipmentView()
	{
		Scene currentScene = myViews.get("RemoveEquipmentView");

		if (currentScene == null)
		{
			View newView = ViewFactory.createView("RemoveEquipmentView", this);
			currentScene = new Scene(newView);
			myViews.put("UpdateEquipmentView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}

	}
	

}

