package userinterface;

import controller.Transaction;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, Transaction t)
	{
		if(viewName.equals("LoginView") == true)
		{
			return new LoginView(t);
		}
		else if(viewName.equals("ReceptionistView") == true)
		{
			return new ReceptionistView(t);
		}
		else if(viewName.equals("AddWorkerView") == true)
		{
			return new AddWorkerView(t);
		}
		else if(viewName.equals("AddBorrowerView") == true)
		{
			return new AddBorrowerView(t);
		}
		else if(viewName.equals("AddCategoryView") == true)
		{
			return new AddCategoryView(t);
		}
		else if(viewName.equals("SearchWorkerView") == true)
		{
			return new SearchWorkerView(t);
		}
		else if(viewName.equals("SearchBorrowerView") == true)
		{
			return new SearchBorrowerView(t);
		}
		else if(viewName.equals("SearchCategoryView") == true)
		{
			return new SearchCategoryView(t);
		}
		else if(viewName.equals("WorkerCollectionView") == true)
		{
			return new WorkerCollectionView(t);
		}
		else if(viewName.equals("BorrowerCollectionView") == true)
		{
			return new BorrowerCollectionView(t);
		}
		else if(viewName.equals("CategoryCollectionView") == true)
		{
			return new CategoryCollectionView(t);
		}
		else if(viewName.equals("EquipmentCollectionView") == true)
		{
			return new EquipmentCollectionView(t);
		}
		else if(viewName.equals("CheckOutCollectionView") == true)
		{
			return new CheckOutCollectionView(t);
		}
		else if(viewName.equals("UpdateWorkerView") == true)
		{
			return new UpdateWorkerView(t);
		}
		else if(viewName.equals("UpdateBorrowerView") == true)
		{
			return new UpdateBorrowerView(t);
		}
		else if(viewName.equals("UpdateCategoryView") == true)
		{
			return new UpdateCategoryView(t);
		}
		else if(viewName.equals("AddEquipmentView") == true)
		{
			return new AddEquipmentView(t);
		}
		else if(viewName.equals("SearchBorrowerReserveView") == true)
		{
			return new SearchBorrowerReserveView(t);
		}
		else if(viewName.equals("ReserveEquipmentView") == true)
		{
			return new ReserveEquipmentView(t);
		}
		else
			return null;
	}
}
