package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class CheckOutTableModel
{
	private final SimpleStringProperty Id;
	private final SimpleStringProperty Barcode;
	private final SimpleStringProperty BannerId;
	private final SimpleStringProperty UnitsTaken;
	private final SimpleStringProperty TotalUnitsReturned;
	private final SimpleStringProperty DueDate;
	private final SimpleStringProperty RentDate;

	//----------------------------------------------------------------------------
	public CheckOutTableModel(Vector<String> atData)
	{
		Id =  new SimpleStringProperty(atData.elementAt(0));
		Barcode =  new SimpleStringProperty(atData.elementAt(1));
		BannerId =  new SimpleStringProperty(atData.elementAt(2));
		UnitsTaken =  new SimpleStringProperty(atData.elementAt(3));
		TotalUnitsReturned =  new SimpleStringProperty(atData.elementAt(4));
		DueDate =  new SimpleStringProperty(atData.elementAt(5));
		RentDate =  new SimpleStringProperty(atData.elementAt(6));
	}

	//----------------------------------------------------------------------------
	public String getId() {
		return Id.get();
	}

	//----------------------------------------------------------------------------
	public void setId(String pref) {
		Id.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getBarcode() {
		return Barcode.get();
	}

	//----------------------------------------------------------------------------
	public void setBarcode(String desc) {
		Barcode.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getUnitsTaken() {
		return UnitsTaken.get();
	}

	//----------------------------------------------------------------------------
	public void setUnitsTaken(String desc) {
		UnitsTaken.set(desc);
	}
	//----------------------------------------------------------------------------

	public String getBannerId() {
		return BannerId.get();
	}

	//----------------------------------------------------------------------------
	public void setBannerId(String desc) {
		BannerId.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getTotalUnitsReturned() {
		return TotalUnitsReturned.get();
	}

	//----------------------------------------------------------------------------
	public void setTotalUnitsReturned(String desc) {
		TotalUnitsReturned.set(desc);
	}
	//----------------------------------------------------------------------------
	
	public String getDueDate() {
		return DueDate.get();
	}

	//----------------------------------------------------------------------------
	public void setDueDate(String desc) {
		DueDate.set(desc);
	}

	//----------------------------------------------------------------------------
	public String getRentDate() {
		return RentDate.get();
	}

	//----------------------------------------------------------------------------
	public void setRentDate(String desc) {
		RentDate.set(desc);
	}
}
