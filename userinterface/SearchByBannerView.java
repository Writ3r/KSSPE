// specify the package
package userinterface;

// system imports
import utilities.GlobalVariables;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.PasswordField;
import java.util.Properties;
import java.util.Observer;
import java.util.Observable;

// project imports
import controller.Transaction;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/** The class containing the Add Article Type View  for the Professional Clothes
 *  Closet application 
 */
//==============================================================
public class SearchByBannerView extends View implements Observer
{

	// GUI components
	private TextField userid;
	
	private Button submitButton;
	protected Button cancelButton;

	// For showing error message
	protected MessageView statusLog;
	
	private String userIdText;

	// constructor for this class
	//----------------------------------------------------------
	public SearchByBannerView(Transaction t)
	{

		super(t);

		// create a container for showing the contents
		VBox container = new VBox(10);

		container.setPadding(new Insets(15, 5, 5, 5));
		container.setStyle("-fx-background-color: slategrey");

		// create a Node (Text) for showing the title
		container.getChildren().add(createTitle());

		// create a Node (GridPane) for showing data entry fields
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                          "));

		getChildren().add(container);

		populateFields();
		
		myController.addObserver(this);

	}

	//-------------------------------------------------------------
	protected String getActionText()
	{
		return "Please enter a worker ID";
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

		//bport icon
		//ImageView bportIcon = new ImageView(new Image("/images/BPT_LOGO_All-In-One_Color.png",225,225 ,true,true));
		//bportIcon.setEffect(new DropShadow());
		//container.getChildren().add(bportIcon);

		container.setAlignment(Pos.CENTER);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);
        
		Font myFont = Font.font("copperplate", FontWeight.THIN, 18);    

		GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 25, 20, 0));

		Text userIDLabel = new Text(" Banner ID : ");
			userIDLabel.setFill(Color.GOLD);
			userIDLabel.setFont(myFont);
			userIDLabel.setWrappingWidth(150);
			userIDLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(userIDLabel, 0, 1);

		userid = new TextField();
		grid.add(userid, 1, 1);

		// Text passwordLabel = new Text(" Password : ");
  //           passwordLabel.setFill(Color.GOLD);
		// 	passwordLabel.setFont(myFont);
		// 	passwordLabel.setWrappingWidth(150);
		// 	passwordLabel.setTextAlignment(TextAlignment.RIGHT);
		// grid.add(passwordLabel, 0, 2);

		// password = new PasswordField();
  //               grid.add(password, 1, 2);


		HBox doneCont = new HBox(10);
			doneCont.setAlignment(Pos.CENTER);
		
        ImageView icon = new ImageView(new Image("/images/search.png"));
			icon.setFitHeight(15);
            icon.setFitWidth(15);
				
		submitButton = new Button("Search", icon);
			submitButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			submitButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					processAction(e);    
				}
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
			
		cancelButton = new Button("Cancel", icon);
			cancelButton.setFont(Font.font("Comic Sans", FontWeight.THIN, 14));
			cancelButton.setOnAction((ActionEvent e) -> {
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
        clearOutlines();
        vbox.addEventFilter(KeyEvent.KEY_RELEASED, event->{
			if (event.getCode() == KeyCode.ENTER) {
				submitButton.fire();
			}
        });
				
		return vbox;
	}
	
	//processes whether banner is legitimate
	public void processAction(Event evt)
	{
		clearErrorMessage();
        clearOutlines();

		String useridEntered = userid.getText();

		if ((useridEntered == null) || (useridEntered.length() == 0))
		{
			displayErrorMessage("Please enter a banner id!");
			userid.requestFocus();
		}
		else if(useridEntered.length() != GlobalVariables.BANNERID_LENGTH)
		{
			displayErrorMessage("Please enter a valid bannerid!");
			userid.requestFocus();
		}
		else
		{
			try 
			{ 
				// checking valid integer using parseInt() method 
				Integer.parseInt(useridEntered);

				processBanner(useridEntered);
				
			}  
			catch (NumberFormatException e)  
			{ 
				displayErrorMessage("Please enter a valid bannerid!");
				userid.requestFocus();
			} 
			
		}

	}
	
	/**
	 * Process userid and pwd supplied when Submit button is hit.
	 */
	//----------------------------------------------------------
	private void processBanner(String useridString)
	{
		Properties props = new Properties();
		props.setProperty("BannerId", useridString);

		// clear fields for next time around
		userIdText = userid.getText();
		userid.setText("");

		myController.stateChangeRequest("Login", props);
	}

	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}
        
    private void clearOutlines(){
        userid.setStyle("-fx-border-color: transparent; -fx-focus-color: darkgreen;");
    }
	
	public void populateFields()
	{
		clearValues();
        clearErrorMessage();
	}

    public void clearValues()
    {
        userid.clear();
    }
	
	public void update(Observable o, Object value)
	{
		clearErrorMessage();

		if((String)value != "")           //if there was an error, replace the banner id. 
			userid.setText(userIdText);
		
		displayErrorMessage((String)value);
	}

	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}



