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

/** The class containing the Add Borrower View for the KSSPE
 *  application 
 */
//==============================================================
public class UpdateBorrowerView extends AddBorrowerView
{
	
	protected ComboBox<String> blockStatus;
	protected TextField penalty;
	

	public UpdateBorrowerView(Transaction t)
	{
		super(t);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** UPDATE BORROWER **";
	}
	
	//--------------------------------------------------------------
	public void populateFields()
	{
		Text penaltyLabel = new Text(" Penalty ($) : ");
			penaltyLabel.setFill(Color.GOLD);
			penaltyLabel.setFont(myFont);
			penaltyLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(penaltyLabel, 0, 3);

		penalty = new TextField();
			penalty.setMinWidth(150);
			penalty.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(penalty, 1, 3);
		
		
		Text blockStatusLabel = new Text(" Block Status : ");
			blockStatusLabel.setFill(Color.GOLD);
			blockStatusLabel.setFont(myFont);
			blockStatusLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(blockStatusLabel, 2, 3);

		blockStatus = new ComboBox();
			blockStatus.setMinWidth(150);
			blockStatus.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
			blockStatus.getItems().addAll(
				"Blocked",
				"Unblocked"
			);

		grid.add(blockStatus, 3, 3);
		
		penalty.setStyle("-fx-border-color: transparent;  -fx-focus-color: green;");
		blockStatus.setStyle("-fx-border-color: transparent;  -fx-focus-color: green;");
		
		
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
		
		String notesText = (String)myController.getState("Notes");
		if (notesText != null)
		{
			notes.setText(notesText);
		}
		
		String penaltyText = (String)myController.getState("Penalty");
		if (penaltyText != null)
		{
			penalty.setText(penaltyText);
		}
		
		String blockText = (String)myController.getState("BlockStatus");
		if (blockText != null)
		{
			blockStatus.setValue(blockText);
		}

		submitButton.setText("Update"); //fix submitbutton
		ImageView icon = new ImageView(new Image("/images/savecolor.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton.setGraphic(icon);
		
	}
	
	
	protected void processBannerId(String BannerId)
	{
		
		
	}

	//-----------------------------------------------------------------
	protected void sendToController()
	{
		clearErrorMessage();
		
		String BannerID = bannerId.getText();
		String FirstName = firstName.getText();
		String LastName = lastName.getText();
		String Email = email.getText();
		String PhoneNumber = phoneNumber.getText();
		String Notes = notes.getText();
		String BlockStatus = blockStatus.getValue().toString();
		String Penalty = penalty.getText();
		
		// VALIDATE the data before sending it to the controller
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
							if(Utilities.checkPenalty(Penalty))
							{
								Properties props = new Properties();
								props.setProperty("FirstName", FirstName);
								props.setProperty("LastName", LastName);
								props.setProperty("Email", Email);
								props.setProperty("PhoneNumber", Utilities.formatUSPhoneNumber(PhoneNumber));
								props.setProperty("Notes", Notes);
								props.setProperty("Penalty", Penalty);
								props.setProperty("BlockStatus", BlockStatus);
								myController.stateChangeRequest("BorrowerData", props);
							}
							else
							{
								displayErrorMessage("Please enter a valid penalty.");
								phoneNumber.requestFocus();
							}
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
	
	//-------------------------------
	public void clearValues()
	{
		
	}
	
	//----------------------------------
	private void clearValuesExceptBanner()
	{
		
	}
	
	//----------------------------------
	protected void setDisables()
	{
		
	}

}

//---------------------------------------------------------------
//	Revision History:
//
