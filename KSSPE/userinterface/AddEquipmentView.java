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
import javafx.scene.control.PasswordField;

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
import model.Category;
import model.CategoryCollection;

/** The class containing the Add clothing item View  for the Professional Clothes
 *  Closet application 
 */
//==============================================================
public class AddEquipmentView extends View implements Observer
{

	// GUI components
	protected TextField barcode;
	protected TextField equipmentName;
	protected TextField poor;
	protected ComboBox category;
	protected TextField fair;
	protected TextField notes;
	protected TextField good;
	protected Text actionText;
	protected Text prompt;

	protected HBox barcodeBox;
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public AddEquipmentView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		myController.addObserver(this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** ADD NEW EQUIPMENT **";
	}

	// public void populateFields()
	// {

	// 	String BannerId = bannerId.getText();

		
	// 	Properties props = new Properties();
	// 	props.setProperty("BannerId", BannerId);
		
	// 	myController.stateChangeRequest("removePersonData", props); //cleans it out of past person.
		
	// 	myController.stateChangeRequest("getPersonData", props);
		
	// 	if((Boolean)myController.getState("TestWorker"))
	// 	{
	// 		clearValues();
	// 		bannerId.setText(BannerId);
	// 		setDisables();
	// 		cancelButton.requestFocus();
	// 	}
	// 	else //if the worker doesn't exist, continue on testing if it can autofill or not. 
	// 	{
	// 		removeDisables();
			
	// 		String firstNameState = (String)myController.getState("FirstName");
	// 		String lastNameState = (String)myController.getState("LastName");
	// 		String emailState = (String)myController.getState("Email");
	// 		String phoneState = (String)myController.getState("PhoneNumber");
			
	// 		if(firstNameState != null)
	// 		{
	// 			firstName.setText(firstNameState);
	// 			lastName.setText(lastNameState);
	// 			email.setText(emailState);
	// 			phoneNumber.setText(phoneState);
			
	// 			firstName.setDisable(true);
	// 			lastName.setDisable(true);
	// 			email.setDisable(true);
	// 			phoneNumber.setDisable(true);
				
	// 			password.requestFocus();
	// 		}
	// 		else
	// 		{
	// 			firstName.requestFocus();
	// 		}
			
	// 		bannerId.setText(BannerId);

	// 	}
		
	// }

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		//The program does not make it here.
		VBox container = new VBox(10);
		container.setPadding(new Insets(1, 10, 1, 10));
		
        Text clientText = new Text("KSSPE DEPARTMENT");
			clientText.setFont(Font.font("Copperplate", FontWeight.EXTRA_BOLD, 36));
			clientText.setEffect(new DropShadow());
			clientText.setTextAlignment(TextAlignment.CENTER);
			clientText.setFill(Color.WHITESMOKE);
		container.getChildren().add(clientText);

		Text titleText = new Text(" Reservation Management System ");
			titleText.setFont(Font.font("Copperplate", FontWeight.THIN, 28));
			titleText.setTextAlignment(TextAlignment.CENTER);
			titleText.setFill(Color.GOLD);
		container.getChildren().add(titleText);

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		container.getChildren().add(blankText);

		actionText = new Text("     " + getActionText() + "       ");
			actionText.setFont(Font.font("Copperplate", FontWeight.BOLD, 22));
			actionText.setWrappingWidth(450);
			actionText.setTextAlignment(TextAlignment.CENTER);
			actionText.setFill(Color.DARKGREEN);
		container.getChildren().add(actionText);
		
		container.setAlignment(Pos.CENTER);

		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		Font myFont = Font.font("Copperplate", FontWeight.THIN, 16);
		Font barcodeFont = Font.font("copperplate", FontWeight.BOLD, 18);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);

		
		barcodeBox = new HBox(10);
		barcodeBox.setAlignment(Pos.CENTER);
		barcodeBox.setPadding(new Insets(0, 20, 10, 0));
		
		Text barcodeLabel = new Text("Barcode :");
			barcodeLabel.setFill(Color.GOLD);
			barcodeLabel.setFont(barcodeFont);
			barcodeLabel.setUnderline(true);
			barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
		barcodeBox.getChildren().add(barcodeLabel);

		barcode = new TextField();
		barcode.setMinWidth(150);
		//barcode.setMaxLength();

			barcode.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				setDisables();
				
				if(Utilities.checkBarcode(barcode.getText()))
				{
					removeDisables();
					//populateFields();
				}
				else
				{
					clearValuesExceptBarcode();
				}
			});


		barcodeBox.getChildren().add(barcode);
		

		GridPane grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 25, 15));
			grid.setAlignment(Pos.CENTER);

		
		Text equipmentNameLabel = new Text(" Equipment Name : ");
			equipmentNameLabel.setFill(Color.GOLD);
			equipmentNameLabel.setFont(myFont);
			equipmentNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(equipmentNameLabel, 0, 1);
		
		equipmentName = new TextField();
			equipmentName.setMinWidth(150);
			equipmentName.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(equipmentName, 1, 1);



		Text categoryLabel = new Text(" Category : ");
			categoryLabel.setFill(Color.GOLD);
			categoryLabel.setFont(myFont);
			categoryLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(categoryLabel, 0, 2);

		category = new ComboBox();	
		category.setPromptText("Choose Category");
		category.setMinWidth(150);
		grid.add(category, 1, 2);

		fillCategoryComboBox();

		Text poorLabel = new Text(" Poor Condition Count : ");
			poorLabel.setFill(Color.GOLD);
			poorLabel.setFont(myFont);
			poorLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(poorLabel, 2, 1);

		poor = new TextField();
			poor.setMinWidth(50);
			poor.setMaxWidth(50);
			poor.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(poor, 3, 1);


		Text fairLabel = new Text(" Fair Condition Count : ");
			fairLabel.setFill(Color.GOLD);
			fairLabel.setFont(myFont);
			fairLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(fairLabel, 2, 2);

		fair = new TextField();
			fair.setMinWidth(50);
			fair.setMaxWidth(50);
			fair.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(fair, 3, 2);


		Text goodLabel = new Text(" Good Condition Count : ");
			goodLabel.setFill(Color.GOLD);
			goodLabel.setFont(myFont);
			goodLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(goodLabel, 2, 3);

		good = new TextField();
			good.setMinWidth(50);
			good.setMaxWidth(50);
			good.addEventFilter(KeyEvent.KEY_RELEASED, event->{
					clearErrorMessage();
			});
		grid.add(good, 3, 3);


		Text notesLabel = new Text(" Notes : ");
		notesLabel.setFill(Color.GOLD);
		notesLabel.setFont(myFont);
		notesLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(notesLabel, 0, 3);
		
		
		notes = new TextField();
			notes.setMinWidth(150);
			notes.setMaxWidth(150);
			notes.addEventFilter(KeyEvent.KEY_RELEASED, event->{
					clearErrorMessage();
			});
		grid.add(notes, 1, 3);

		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/pluscolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Add", icon);
			submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			submitButton.setOnAction((ActionEvent e) -> {
				sendToController();
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				submitButton.setEffect(new DropShadow());
			});
			submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				submitButton.setEffect(null);
			});
		doneCont.getChildren().add(submitButton);
		
		icon = new ImageView(new Image("/images/return.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		cancelButton = new Button("Return", icon);
			cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			cancelButton.setOnAction((ActionEvent e) -> {
				clearErrorMessage();
				myController.stateChangeRequest("CancelTransaction", null);
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
				cancelButton.setEffect(new DropShadow());
			});
			cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
				cancelButton.setEffect(null);
			});
		doneCont.getChildren().add(cancelButton);
		
		vbox.getChildren().add(barcodeBox);
		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);
	
		setOutlines();
		
		setDisables(); //disable everything until the bannerId is entered.
               
		return vbox;
	}

	//-----------------------------------------------------------------------------------
	private void sendToController()
	{
		clearErrorMessage();
		
		String Barcode = barcode.getText();
		String EquipmentName = equipmentName.getText();
		String PoorCount = poor.getText();
		String FairCount = fair.getText();
		String GoodCount = good.getText();
		String Note = notes.getText();
		String Category;
		int AvailableCount;
		int InStockCount;
		
		if(Utilities.checkBarcode(Barcode)) 
		{
			if(Utilities.checkEquipmentName(EquipmentName))
			{
				if(Utilities.checkPoorCount(PoorCount))
				{
					if(category.getValue() != null)
					{
						if(Utilities.checkFairCount(FairCount))
						{
							if(Utilities.checkGoodCount(GoodCount))
							{
								if(Utilities.checkNotes(Note))  
								{
									AvailableCount = Integer.parseInt(PoorCount) + Integer.parseInt(FairCount) + Integer.parseInt(GoodCount);
									InStockCount = Integer.parseInt(PoorCount) + Integer.parseInt(FairCount) + Integer.parseInt(GoodCount);
									Category = category.getValue().toString();
							
									Properties props = new Properties();
									props.setProperty("Barcode", Barcode);
									props.setProperty("Name", EquipmentName);
									props.setProperty("PoorCount", PoorCount);
									props.setProperty("FairCount", FairCount);
									props.setProperty("CategoryName", Category);
									props.setProperty("GoodCount", GoodCount);
									props.setProperty("Notes", Note);
									props.setProperty("AvailableCount", Integer.toString(AvailableCount));
									props.setProperty("InStockCount", Integer.toString(InStockCount));
									removeDisables();
									myController.stateChangeRequest("EquipmentData", props);
									setDisables();
									
								}
								else
								{
									displayErrorMessage("Notes are too long. Must be below 50 characters.");
									notes.requestFocus();
								}
							}
							else
							{
								displayErrorMessage("Please enter a valid good count.");
								good.requestFocus();
							}
						}
						else
						{
							displayErrorMessage("Please enter a valid fair count.");
							fair.requestFocus();
						}
					}
					else
					{
						displayErrorMessage("Please enter a category.");
						category.requestFocus();
					}
				}
				else
				{
					displayErrorMessage("Please enter a valid poor count.");
					poor.requestFocus();
				}
			}
			else
			{
				displayErrorMessage("Please enter a valid name.");
				equipmentName.requestFocus();
			}
		}
		else
		{
			displayErrorMessage("Please enter a valid Barcode.");
			barcode.requestFocus();
		}
		
	}
	
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//--------------------------------------------------------------------------------------
	public void fillCategoryComboBox()
	{

		try
		{
			CategoryCollection categoryCollection = new CategoryCollection();
		
			categoryCollection.findAll();
			Vector entryList = (Vector)categoryCollection.getState("Categories");

			if(entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while(entries.hasMoreElements() == true)
				{
					Category c = (Category)entries.nextElement();
					String categoryName = (String)c.getState("Name");
					category.getItems().add(categoryName);
				}
			} 
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

	//-------------------------------------------------------------------------------------------
	public void clearValues()
	{
		barcode.clear();
		equipmentName.clear();
		poor.clear();
		fair.clear();
		good.clear();
		notes.clear();
		category.getSelectionModel().select(null);
	}
	
	//------------------------------------------------------------------------------------------
	private void clearValuesExceptBarcode()
	{
		equipmentName.clear();
		poor.clear();
		fair.clear();
		good.clear();
		notes.clear();
		category.getSelectionModel().select(null);
	}
	
	//------------------------------------------------------------------------------------------
	private void removeDisables()
	{
		equipmentName.setDisable(false);
		poor.setDisable(false);
		category.setDisable(false);
		fair.setDisable(false);
		good.setDisable(false);
		notes.setDisable(false);
	}
	
	//------------------------------------------------------------------------------------------
	private void setDisables()
	{
		equipmentName.setDisable(true);
		poor.setDisable(true);
		category.setDisable(true);
		fair.setDisable(true);
		good.setDisable(true);
		notes.setDisable(true);
	}

	//--------------------------------------------------------------------------------------------
	private void setOutlines()
	{
		barcode.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		equipmentName.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		poor.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		category.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
		fair.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		good.setStyle("-fx-border-color: transparent;  -fx-focus-color: darkgreen;");
		notes.setStyle("-fx-border-color: transparent;  -fx-focus-color: darkgreen;");
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		String val = (String)value;
		if (val.startsWith("ERR") == true)
		{
			displayErrorMessage(val);
		}
		else
		{
			clearValues();
			displayMessage(val);
		}
		
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}

//---------------------------------------------------------------
//	Revision History:
//
