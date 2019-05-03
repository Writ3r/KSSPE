package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Observer;
import java.util.Observable;

// project imports
import impresario.IModel;
import controller.Transaction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import model.CheckOut;
import model.CheckOutCollection;

//==============================================================================
public class CheckOutCollectionView extends View implements Observer
{
	protected TableView<CheckOutTableModel> tableOfCheckOuts;
	protected Button cancelButton;
	protected Button submitButton;
	protected MessageView statusLog;
	protected Text actionText; 
        
	//--------------------------------------------------------------------------
	public CheckOutCollectionView(Transaction t)
	{
		super(t);

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setStyle("-fx-background-color: slategrey");
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                            "));

		getChildren().add(container);
		
		populateFields();
		
		myController.addObserver(this);
		
		tableOfCheckOuts.getSelectionModel().select(0); //autoselect first element
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{

		ObservableList<CheckOutTableModel> tableData = FXCollections.observableArrayList();
		try
		{
	
			CheckOutCollection checkoutCollection = 
					(CheckOutCollection)myController.getState("CheckOutList");

			Vector entryList = (Vector)checkoutCollection.getState("CheckOuts");
			
			if (entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while (entries.hasMoreElements() == true)
				{
					CheckOut nextCO = (CheckOut)entries.nextElement();

					Vector<String> view = nextCO.getEntryListView();

					// add this list entry to the list
					CheckOutTableModel nextTableRowData = new CheckOutTableModel(view);
					tableData.add(nextTableRowData);

				}
				if(entryList.size() == 1)
					actionText.setText(entryList.size()+" Reservation Found!");
				else 
					actionText.setText(entryList.size()+" Reservations Found!");

				actionText.setFill(Color.LIGHTGREEN);
			}
			else
			{
				actionText.setText("No Reservations Found!");
				actionText.setFill(Color.FIREBRICK);
			}

			tableOfCheckOuts.setItems(tableData);
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}

	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		VBox container = new VBox(10);
		container.setPadding(new Insets(1, 10, 12, 10));
		
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
	
	protected String getActionText()
	{
		return "** CHOOSE ONE RESERVATION TO RETURN **";
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		tableOfCheckOuts = new TableView<CheckOutTableModel>();
		tableOfCheckOuts.setEffect(new DropShadow());
		tableOfCheckOuts.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-selection-bar: gold;");
		tableOfCheckOuts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		
		TableColumn barcodePrefixColumn = new TableColumn("Barcode") ;
		barcodePrefixColumn.setMinWidth(116.6);
		barcodePrefixColumn.setStyle(" -fx-alignment: CENTER;");
		barcodePrefixColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("Barcode"));


		TableColumn bannerColumn = new TableColumn("Banner ID") ;
		bannerColumn.setMinWidth(116.6);
		bannerColumn.setStyle(" -fx-alignment: CENTER;");
		bannerColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("BannerId"));
		
		TableColumn takenColumn = new TableColumn("Units Taken") ;
		takenColumn.setMinWidth(116.6);
		takenColumn.setStyle(" -fx-alignment: CENTER;");
		takenColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("UnitsTaken"));

		TableColumn returnedColumn = new TableColumn("Total Units Returned") ;
		returnedColumn.setMinWidth(116.6);
		returnedColumn.setStyle(" -fx-alignment: CENTER;");
		returnedColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("TotalUnitsReturned"));

		TableColumn dueColumn = new TableColumn("Due Date") ;
		dueColumn.setMinWidth(116.6);
		dueColumn.setStyle(" -fx-alignment: CENTER;");
		dueColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("DueDate"));

		TableColumn rentColumn = new TableColumn("Check Out Date") ;
		rentColumn.setMinWidth(116.6);
		rentColumn.setStyle(" -fx-alignment: CENTER;");
		rentColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("RentDate"));

		tableOfCheckOuts.getColumns().addAll(bannerColumn, barcodePrefixColumn, takenColumn, returnedColumn, dueColumn, rentColumn);

		tableOfCheckOuts.setOnMousePressed((MouseEvent event) -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
				processCheckOutSelected();
			}
		});
		ImageView icon = new ImageView(new Image("/images/check.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		submitButton = new Button("Select",icon);
		submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		submitButton.requestFocus();
		submitButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			processCheckOutSelected();
		});
		submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			submitButton.setEffect(new DropShadow());
		});
		submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			submitButton.setEffect(null);
		});
		icon = new ImageView(new Image("/images/return.png"));
		icon.setFitHeight(15);
		icon.setFitWidth(15);
		cancelButton = new Button("Return", icon);
		cancelButton.setGraphic(icon);
		cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
		cancelButton.setOnAction((ActionEvent e) -> {
			clearErrorMessage();
			myController.stateChangeRequest("CancelCheckOutList", null);
		});
		cancelButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
			cancelButton.setEffect(new DropShadow());
		});
		cancelButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
			cancelButton.setEffect(null);
		});
		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.CENTER);
                btnContainer.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                    btnContainer.setStyle("-fx-background-color: GOLD");
		});
                btnContainer.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                    btnContainer.setStyle("-fx-background-color: SLATEGREY");
		});
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(cancelButton);

		tableOfCheckOuts.setPrefHeight(275);
        tableOfCheckOuts.setMaxWidth(350);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(250, 150);
		scrollPane.setContent(tableOfCheckOuts); // should we use this? (Probably not - FX tables come with their own scroll pane)
		
		vbox.getChildren().add(tableOfCheckOuts);
		vbox.getChildren().add(btnContainer);
		vbox.setPadding(new Insets(10,10,10,10));
                vbox.setAlignment(Pos.CENTER);

		return vbox;
	}

	//--------------------------------------------------------------------------
	protected void processCheckOutSelected()
	{
		CheckOutTableModel selectedItem = tableOfCheckOuts.getSelectionModel().getSelectedItem();

		if(selectedItem != null)
		{
			String reservationId = selectedItem.getId();

			myController.stateChangeRequest("CheckOutSelected", reservationId);
		}
	}
	
    //---------------------------------------------------------------------------
	// private void displayRemoveAlert(){
	// 	clearErrorMessage();
	// 	Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"BannerId: "+tableOfBorrowers.getSelectionModel().getSelectedItem().getBannerId()
	// 			+"\nFirstName: "+tableOfBorrowers.getSelectionModel().getSelectedItem().getFirstName()
	// 			+"\nLastName: "+tableOfBorrowers.getSelectionModel().getSelectedItem().getLastName(), ButtonType.YES, ButtonType.NO);
	// 	alert.setHeaderText(null);
	// 	alert.setTitle("Remove Borrower");
	// 	alert.setHeaderText("Are you sure want to remove this Borrower?");
	// 	((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("images/BPT_LOGO_All-In-One_Color.png"));
	// 	alert.showAndWait();

	// 	if (alert.getResult() == ButtonType.YES) {
	// 		processBorrowerSelected();
	// 	}
	// }
       
	//--------------------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}
	
	//--------------------------------------------------------------------------
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		String val = (String)value;
		if (val.startsWith("ERR") == true)
		{
			displayErrorMessage(val);
			getEntryTableModelValues();
		}
		else
		{
			displayMessage(val);
			getEntryTableModelValues();
		}
	}
	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
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
