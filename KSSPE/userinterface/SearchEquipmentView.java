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
import javafx.scene.layout.ColumnConstraints;

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

/** The class containing the Search Category View for the KSSPE
 *  application 
 */
//==============================================================
public class SearchEquipmentView extends View implements Observer
{

	// GUI components
	protected TextField barcode;
	protected TextField name;

	protected Text actionText;
	protected Text prompt;

	protected HBox bannerBox;
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public SearchEquipmentView(Transaction t)
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
		return "** SEARCH FOR EQUIPMENT **";
	}

	public void populateFields()
	{
		//get current autoinc for category
	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
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
		
		Font myFont = Font.font("copperplate", FontWeight.THIN, 18);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);

		
		GridPane grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 25, 20));
			grid.setAlignment(Pos.CENTER);
			

		Text barcodeHeader = new Text("Barcode :"); //autofill with newest autoinc number
			barcodeHeader.setFill(Color.GOLD);
			barcodeHeader.setFont(myFont);
			barcodeHeader.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodeHeader, 0, 1);
			
			
		barcode = new TextField();
			barcode.setMinWidth(150);
			barcode.setOnKeyTyped(event ->{
				if(barcode.getText().length() > GlobalVariables.BARCODE_LENGTH - 1)
					event.consume();
			});
			barcode.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(barcode, 1, 1);
		
		
		HBox orCont = new HBox(10);
			orCont.setAlignment(Pos.CENTER);
			
		Text orHeader = new Text("---------- OR SEARCH BY ----------"); //autofill with newest autoinc number
			orHeader.setFill(Color.GOLD);
			orHeader.setFont(myFont);
			orHeader.setTextAlignment(TextAlignment.RIGHT);
		orCont.getChildren().add(orHeader);
		
		
		GridPane grid2 = new GridPane();
			grid2.setHgap(15);
			grid2.setVgap(15);
			grid2.setPadding(new Insets(10, 20, 30, 20));
			grid2.setAlignment(Pos.CENTER);
		
		
		Text nameHeader = new Text("Equipment Name :"); //autofill with newest autoinc number
			nameHeader.setFill(Color.GOLD);
			nameHeader.setFont(myFont);
			nameHeader.setTextAlignment(TextAlignment.RIGHT);
		grid2.add(nameHeader, 0, 1);
		
		name = new TextField();
			name.setMinWidth(150);
			name.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid2.add(name, 1, 1);
		
		//---------------------------------------------------------------------------------

		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/searchcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Search", icon);
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
		
		vbox.getChildren().add(grid);
		vbox.getChildren().add(orCont);
		vbox.getChildren().add(grid2);
		vbox.getChildren().add(doneCont);
	
		setOutlines();
               
		return vbox;
	}

	private void sendToController()
	{
		clearErrorMessage();
		
		String Barcode = barcode.getText();
		String Name = name.getText();
		Properties props = new Properties();
		
		if(!Barcode.equals(""))
		{
			if(Utilities.checkIsNumber(Barcode))
			{
				props.setProperty("Barcode", Barcode);
				myController.stateChangeRequest("SearchEquipment", props);			
			}
			else
			{
				displayErrorMessage("Please enter a valid Barcode (Digits).");
				barcode.requestFocus();
			}
			
		}
		else if(!Name.equals(""))
		{
			if(Utilities.checkEquipmentName(Name))
			{
				props.setProperty("Name", Name);
				myController.stateChangeRequest("SearchEquipment", props);						
			}
			else
			{
				displayErrorMessage("Please enter a valid name.");
				name.requestFocus();
			}
		}
		else
		{
			myController.stateChangeRequest("SearchEquipment", props);
		}
	}
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	public void clearValues()
	{
		barcode.clear();
		name.clear();
	}

	private void setOutlines()
	{
		barcode.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		name.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
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
			barcode.requestFocus();
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
