package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class WorkerTableModel
{
	private final SimpleStringProperty BannerId;
	private final SimpleStringProperty FirstName;
	private final SimpleStringProperty LastName;

	//----------------------------------------------------------------------------
	public WorkerTableModel(Vector<String> atData)
	{
		BannerId =  new SimpleStringProperty(atData.elementAt(0));
		FirstName =  new SimpleStringProperty(atData.elementAt(1));
		LastName =  new SimpleStringProperty(atData.elementAt(2));
	}

	//----------------------------------------------------------------------------
	public String getBannerId() {
		return BannerId.get();
	}

	//----------------------------------------------------------------------------
	public void setBannerId(String pref) {
		BannerId.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getFirstName() {
		return FirstName.get();
	}

	//----------------------------------------------------------------------------
	public void setFirstName(String desc) {
		FirstName.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getLastName() {
		return LastName.get();
	}

	//----------------------------------------------------------------------------
	public void setLastName(String desc) {
		LastName.set(desc);
	}
}
