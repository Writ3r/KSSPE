package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class CheckOutTableModel
{
	private final SimpleStringProperty Id;
	private final SimpleStringProperty BannerId;
	private final SimpleStringProperty Barcode;
	private final SimpleStringProperty UnitsTaken;
	private final SimpleStringProperty TotalUnitsReturned;
	private final SimpleStringProperty RentDate;
	private final SimpleStringProperty DueDate;
	private final SimpleStringProperty CheckOutWorkerId;

	//----------------------------------------------------------------------------
	public CheckOutTableModel(Vector<String> atData)
	{
		Id =  new SimpleStringProperty(atData.elementAt(0));
		BannerId =  new SimpleStringProperty(atData.elementAt(1));
		Barcode =  new SimpleStringProperty(atData.elementAt(2));
		UnitsTaken =  new SimpleStringProperty(atData.elementAt(3));
		TotalUnitsReturned =  new SimpleStringProperty(atData.elementAt(4));
		RentDate = new SimpleStringProperty(atData.elementAt(5));
		DueDate = new SimpleStringProperty(atData.elementAt(6));
		CheckOutWorkerId = new SimpleStringProperty(atData.elementAt(7));
	}

	//----------------------------------------------------------------------------
	public String getBarcode() {
		return Barcode.get();
	}

	//----------------------------------------------------------------------------
	public void setBarcode(String pref) {
		Barcode.set(pref);
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
	public String getBannerId() {
		return BannerId.get();
	}

	//----------------------------------------------------------------------------
	public void setBannerId(String pref) {
		BannerId.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getUnitsTaken() {
		return UnitsTaken.get();
	}

	//----------------------------------------------------------------------------
	public void setUnitsTaken(String pref) {
		UnitsTaken.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getTotalUnitsReturned() {
		return TotalUnitsReturned.get();
	}

	//----------------------------------------------------------------------------
	public void setTotalUnitsReturned(String pref) {
		TotalUnitsReturned.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getRentDate() {
		return RentDate.get();
	}

	//----------------------------------------------------------------------------
	public void setRentDate(String pref) {
		RentDate.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getDueDate() {
		return DueDate.get();
	}

	//----------------------------------------------------------------------------
	public void setDueDate(String pref) {
		DueDate.set(pref);
	}

	//----------------------------------------------------------------------------
	public String getCheckOutWorkerId() {
		return CheckOutWorkerId.get();
	}

	//----------------------------------------------------------------------------
	public void setCheckOutWorkerId(String pref) {
		CheckOutWorkerId.set(pref);
	}

}
