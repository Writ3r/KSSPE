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
			CheckOutCollection checkOutCollection = 
					(CheckOutCollection)myController.getState("CheckOutList");

			Vector entryList = (Vector)checkOutCollection.getState("CheckOuts");
			
			if (entryList.size() > 0)
			{
				Enumeration entries = entryList.elements();

				while (entries.hasMoreElements() == true)
				{
					CheckOut nextC = (CheckOut)entries.nextElement();

					Vector<String> view = nextC.getEntryListView();

					// add this list entry to the list
					CheckOutTableModel nextTableRowData = new CheckOutTableModel(view);
					tableData.add(nextTableRowData);

				}

				if(entryList.size() == 1)
					actionText.setText(entryList.size()+" Record Found!");
				else
					actionText.setText(entryList.size()+" Records Found!");
				actionText.setFill(Color.LIGHTGREEN);
			}
			else
			{
				actionText.setText("No Records Found!");
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
		return "** LIST OF RECORDS **";
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

		TableColumn barcodeColumn = new TableColumn("Barcode") ;
		barcodeColumn.setMinWidth(116.6);
		barcodeColumn.setStyle(" -fx-alignment: CENTER;");
		barcodeColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("Barcode"));

		TableColumn bannerIdColumn = new TableColumn("BannerId") ;
		bannerIdColumn.setMinWidth(116.6);
		bannerIdColumn.setStyle(" -fx-alignment: CENTER;");
		bannerIdColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("BannerId"));

		TableColumn unitsTakenColumn = new TableColumn("Units Taken") ;
		unitsTakenColumn.setMinWidth(116.6);
		unitsTakenColumn.setStyle(" -fx-alignment: CENTER;");
		unitsTakenColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("UnitsTaken"));

		TableColumn unitsReturnedColumn = new TableColumn("Units Returned") ;
		unitsReturnedColumn.setMinWidth(116.6);
		unitsReturnedColumn.setStyle(" -fx-alignment: CENTER;");
		unitsReturnedColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("TotalUnitsReturned"));

		TableColumn rentDateColumn = new TableColumn("Rented on") ;
		rentDateColumn.setMinWidth(116.6);
		rentDateColumn.setStyle(" -fx-alignment: CENTER;");
		rentDateColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("RentDate"));

		TableColumn dueDateColumn = new TableColumn("Due on") ;
		dueDateColumn.setMinWidth(116.6);
		dueDateColumn.setStyle(" -fx-alignment: CENTER;");
		dueDateColumn.setCellValueFactory(
				new PropertyValueFactory<CheckOutTableModel, String>("DueDate"));

		tableOfCheckOuts.getColumns().addAll(barcodeColumn, bannerIdColumn, unitsTakenColumn, unitsReturnedColumn, rentDateColumn, dueDateColumn);

		ImageView icon = new ImageView(new Image("/images/return.png"));
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

		btnContainer.getChildren().add(cancelButton);

		tableOfCheckOuts.setPrefHeight(275);
        tableOfCheckOuts.setMaxWidth(3000);
		
		vbox.getChildren().add(tableOfCheckOuts);
		vbox.getChildren().add(btnContainer);
		vbox.setPadding(new Insets(10,10,10,10));
                vbox.setAlignment(Pos.CENTER);

		return vbox;
	}
       
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
