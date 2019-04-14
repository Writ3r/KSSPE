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
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;

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
public class ReserveEquipmentView extends View implements Observer
{

	// GUI components
	protected TextField barcode;
	protected TextField count;
	protected TextField dueDate;
	
	protected GridPane grid;
	protected Font myFont;

	protected Text actionText;
	protected Text prompt;
	
	protected HBox doneCont;
	protected Button submitButton;
	protected Button cancelButton;

	protected MessageView statusLog;

	// constructor for this class -- takes a controller object
	//----------------------------------------------------------
	public ReserveEquipmentView(Transaction t)
	{
		super(t);

		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		container.getChildren().add(createTitle());

		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);
		
		populateFields();
		
		checkForPenaltiesAndBlocks(); //checks if the user has penalties/blocks. Puts up an alert if so. 

		myController.addObserver(this);
	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "** RESERVE EQUIPMENT **";
	}
	
	public void populateFields()
	{
		
	}

	protected void processBarcode(String barcode)
	{
		
		
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
		
		myFont = Font.font("Copperplate", FontWeight.THIN, 17);
		Font bannerFont = Font.font("copperplate", FontWeight.BOLD, 17);   

		Text blankText = new Text("  ");
			blankText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
			blankText.setWrappingWidth(350);
			blankText.setTextAlignment(TextAlignment.CENTER);
			blankText.setFill(Color.WHITE);
		vbox.getChildren().add(blankText);
		
		
		
		grid = new GridPane();
			grid.setHgap(15);
			grid.setVgap(15);
			grid.setPadding(new Insets(0, 20, 20, 15));
			grid.setAlignment(Pos.CENTER);
		
		Text barcodeLabel = new Text("Barcode :");
			barcodeLabel.setFill(Color.GOLD);
			barcodeLabel.setFont(bannerFont);
			barcodeLabel.setUnderline(true);
			barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodeLabel, 0, 1);
		
		barcode = new TextField();
			barcode.setMinWidth(110);
			barcode.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
				if(Utilities.checkBarcode(barcode.getText()))
				{
					processBarcode(barcode.getText());
				}
			});
		grid.add(barcode, 1, 1);
		
		Text countLabel = new Text("Units Taken:");
			countLabel.setFill(Color.GOLD);
			countLabel.setFont(myFont);
			countLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(countLabel, 0, 2);
		
		count = new TextField();
			count.setMinWidth(110);
			count.setMinWidth(110);
			count.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(count, 1, 2);
		
		Text dueLabel = new Text("Due Date :");
			dueLabel.setFill(Color.GOLD);
			dueLabel.setFont(myFont);
			dueLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dueLabel, 0, 3);
		
		dueDate = new TextField();
			dueDate.setMinWidth(110);
			dueDate.setMinWidth(110);
			dueDate.addEventFilter(KeyEvent.KEY_RELEASED, event->{
				clearErrorMessage();
			});
		grid.add(dueDate, 1, 3);
		
		
		//---------------------------------- middle grid done.
		
		
		doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
            doneCont.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: GOLD");
		});
        doneCont.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            doneCont.setStyle("-fx-background-color: SLATEGREY");
		});
		
		ImageView icon = new ImageView(new Image("/images/buyingcolor.png"));
			icon.setFitHeight(15);
			icon.setFitWidth(15);
			
		submitButton = new Button("Reserve", icon);
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
		vbox.getChildren().add(doneCont);
	
		setOutlines();
		
		return vbox;
	}

	//---------------------------------------------------------------------------------------------------
	protected void sendToController()
	{
		
		
	}
	
	
	private void checkForPenaltiesAndBlocks()
	{
		String penalty = (String)myController.getState("Penalty");
		String block = (String)myController.getState("BlockStatus");
		
		if((!penalty.equals("") && penalty != null && !penalty.equals("0")) || (!block.equals("") && block != null && !block.equals("Unblocked")))
		{
			Alert alert = new Alert(Alert.AlertType.ERROR,"BannerId: "+(String)myController.getState("BorrowerBannerId")
					+"\nBlocked Status: "+ block
					+"\nPenalty: " + penalty, ButtonType.OK);
					
			alert.setTitle("Penalized or Blocked Borrower");
			alert.setHeaderText("Borrower has been Blocked or Penalized.");
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("images/BPT_LOGO_All-In-One_Color.png"));
			alert.showAndWait();
		}
	}
	
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void clearValues()
	{
		barcode.clear();
		count.clear();
		dueDate.clear();
		
	}
	
	public void clearValuesExceptDue()
	{
		barcode.clear();
		count.clear();
	}

	//------------------------------------------------------------------------------------------
	private void setOutlines()
	{
		barcode.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		count.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
		dueDate.setStyle("-fx-border-color: transparent; -fx-focus-color: green;");
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
			clearValuesExceptDue();
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
