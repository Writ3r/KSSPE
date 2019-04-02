// specify the package
package userinterface;

// system imports
import utilities.GlobalVariables;
import utilities.Utilities;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.util.Properties;
import java.util.Observer;
import java.util.Observable;

// project imports
import java.util.Enumeration;
import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import controller.Transaction;

/** The class containing the Update Worker View for the KSSPE
* Inventory Management project
 */
//==============================================================
public class UpdateWorkerView extends AddWorkerView
{
	protected ComboBox<String> credential;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public UpdateWorkerView(Transaction t)
	{
		super(t);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** UPDATING WORKER **";
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		removeDisables();
		//DEBUG: I need about a gallon of coffee right now
		String bannerIdText = (String)myController.getState("BannerId");
		if (bannerIdText != null)
		{
			bannerId.setText(bannerIdText);
			bannerId.setDisable(true);
		}
		
		String firstNameText = (String)myController.getState("FirstName");
		if (firstNameText != null)
		{
			firstName.setText(firstNameText);
		}
		
		String lastNameText = (String)myController.getState("LastName");
		if (lastNameText != null)
		{
			lastName.setText(lastNameText);
		}
		
		String emailText = (String)myController.getState("Email");
		if (emailText != null)
		{
			email.setText(emailText);
		}
		
		String phoneNumberText = (String)myController.getState("PhoneNumber");
		if (phoneNumberText != null)
		{
			phoneNumber.setText(phoneNumberText);
		}

		String passwordText = (String)myController.getState("Password");
		if (passwordText != null)
		{
			password.setText(passwordText);
		}

		String credentialText = (String)myController.getState("Credential");
		if (credentialText != null)
		{
			//credential.setSelectedItem((String) credentialText);
		}

		submitButton.setText("Update"); 
		ImageView icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton.setGraphic(icon);
	}

	protected void sendToController()
	{
		clearErrorMessage();
		
		String BannerID = bannerId.getText();
		String FirstName = firstName.getText();
		String LastName = lastName.getText();
		String Email = email.getText();
		String PhoneNumber = phoneNumber.getText();
		String Password = password.getText();
		String Credential = credential.getValue().toString();
		
		if(Utilities.checkBannerId(BannerID)) 
		{
			if(Utilities.checkName(FirstName))
			{
				if(Utilities.checkName(LastName))
				{
					if(Utilities.checkEmail(Email))
					{
						if(Utilities.checkPhone(PhoneNumber))
						{
								Properties props = new Properties();
								props.setProperty("FirstName", FirstName);
								props.setProperty("LastName", LastName);
								props.setProperty("Email", Email);
								props.setProperty("PhoneNumber", PhoneNumber);
								props.setProperty("Password", Password);
								props.setProperty("Credential", Credential);
								myController.stateChangeRequest("WorkerData", props);
						}
						else
						{
							displayErrorMessage("Please enter a valid phone number.");
							phoneNumber.requestFocus();
						}
					}
					else
					{
						displayErrorMessage("Please enter a valid email.");
						email.requestFocus();
					}
				}
				else
				{
					displayErrorMessage("Please enter a valid last name.");
					lastName.requestFocus();
				}
			}
			else
			{
				displayErrorMessage("Please enter a valid first name.");
				firstName.requestFocus();
			}
		}
		else
		{
			displayErrorMessage("Please enter a valid Banner Id.");
			bannerId.requestFocus();
		}
		
	}

	public void clearValues(){

	}
}


