package utilities;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import utilities.Utilities;

import model.Equipment;

public class ReserveReceipt
{
	// Declare a home directory to save receipts in at the top of the class
	private static final String receiptHomeDirectory = 
			"C:" + File.separator + "Users" + File.separator + 
			"Luke" + File.separator + "Desktop" + File.separator + 
			"Receipts";

	// Hold the receipt to save as a hash table
	private Hashtable receipt = new Hashtable();
	private int CHECKIN_TIME_HOUR;
	private int CHECKIN_TIME_MINUTE;

	// CREATE the Hashtable if it does not exist in the constructor of whichever class is building up the receipt

	//Put stuff in the Hashtable to build up the receipt as appropriate
	// For example, the JRB people want the "Cage worker" name and banner id to be printed as part of the receipt,
	// so they must be put in the receipt

	public ReserveReceipt(Properties p, Vector rentals) throws Exception
	{
		receipt.put("WorkerName", p.getProperty("WorkerName"));
		receipt.put("WorkerBannerId", p.getProperty("WorkerBannerId"));
		receipt.put("BorrowerName", p.getProperty("BorrowerName"));
		receipt.put("BorrowerBannerId", p.getProperty("BorrowerBannerId"));
		receipt.put("Rentals", rentals);
		
		CHECKIN_TIME_HOUR = 12;
		CHECKIN_TIME_MINUTE = 0;
		
		printReceipt();
	}

	//----------------------------------------------------------------
	private void printReceipt() throws Exception
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		int nowYear = cal.get(Calendar.YEAR);
		int nowMonth = cal.get(Calendar.MONTH);
			
		String yearValue = (new Integer(nowYear)).toString();
		String monthName = Utilities.mapMonthToString(nowMonth);
			
		String receiptDirectoryName = receiptHomeDirectory + File.separator +
				"CheckoutReceipts" + File.separator + yearValue + File.separator +
				monthName; 
				
		String nowTimeText = dateFormatter.format(cal.getTime());	
		receipt.put("ReceiptTime", nowTimeText);
		
		String borrowerName = (String)receipt.get("BorrowerName");
		String receiptFileName = "";
				
		File receiptDirectory = new File(receiptDirectoryName);
		if (receiptDirectory.exists() == false)
		{
			boolean flag = receiptDirectory.mkdirs();
				
			if (flag == true)
			{
				receiptFileName = receiptDirectoryName + File.separator + 
						 borrowerName + "Checkout" + nowTimeText + ".txt";
			}
			else
			{
				throw new Exception("Could not create directory to store checkout receipt"); 
			}
		}
		else
		{
			receiptFileName = receiptDirectoryName + File.separator + 
				borrowerName + "Checkout" + nowTimeText + ".txt";
		}

		writeReceiptDataToFile(receiptFileName);
		//printFileToDefaultPrinter(receiptFileName);
		
	}

	//----------------------------------------------------------------
	private void writeReceiptDataToFile(String fileName) throws Exception
	{
		BufferedWriter outputFile = null;
			try
			{
				outputFile = new BufferedWriter(new FileWriter(new File(fileName)));
				
				outputFile.newLine();
				outputFile.write("                                KSSPE DEPARTMENT");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("                           EQUIPMENT CHECK-OUT RECEIPT");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Date: " + (String)receipt.get("ReceiptTime"));
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Borrower Name (BannerID): " + (String)receipt.get("BorrowerName") + "(" + (String)receipt.get("BorrowerBannerId") + ")");
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Worker Name (BannerID): " + (String)receipt.get("WorkerName") + "(" + (String)receipt.get("WorkerBannerId") + ")") ;
				outputFile.newLine();
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write(String.format("%20s %25s %20s %20s \r\n", "Barcode", "Name", "Count", "Due Date"));
				outputFile.write("-----------------------------------------------------------------------------------------");
				outputFile.newLine();
				
				// Put all the rentals in
				// Your rentals (aka RESERVATIONS) should be in a COLLECTION (Vector, ArrayList, ....)
				Vector allRentalsInReceipt = (Vector)receipt.get("Rentals");
				if (allRentalsInReceipt != null)
				{
					for (int cnt = 0; cnt < allRentalsInReceipt.size(); cnt++)
					{
						Properties rentalInfo = (Properties)allRentalsInReceipt.elementAt(cnt);
							String barcodeID = (String)rentalInfo.getProperty("Barcode");
							String itemName = (String)rentalInfo.getProperty("Name");
							String count = (String)rentalInfo.getProperty("Count");
							String dueDate = (String)rentalInfo.getProperty("DueDate");
						
						if (itemName.length() > 25)
						{
							itemName = itemName.substring(0,25);
						}
						
						outputFile.write(String.format("%20s %25s %20s %20s \r\n", barcodeID, itemName, count, dueDate));
					}
				}
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());

				SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aaa");
				String nowTimeText = dateFormatter.format(cal.getTime());

				outputFile.newLine();
				outputFile.newLine();
				
				String returnTimeString = "All items must be returned by " + nowTimeText + " on the due date shown above.";
				outputFile.write(returnTimeString);
				outputFile.newLine();
				outputFile.newLine();
				
				cal.setTime(new Date());
				cal.add(cal.MINUTE, 15);
				String futureTimeText = dateFormatter.format(cal.getTime());

				outputFile.write("We will begin to charge fines at " + futureTimeText + " on the due date." );  
			  
				outputFile.newLine();
				outputFile.newLine();
				
				String tailMessage = "IMPORTANT NOTE: Double check that all the items on your receipt" +
				" are there and in good condition. \nYou have 15 minutes to do so, otherwise you will be " +
				"charged for the missing or damaged items.";
					
				outputFile.write(tailMessage);
				outputFile.newLine();
					
				outputFile.close();
				
			}
			catch(IOException e)
			{ 
				throw new Exception("File I/0 error in creating receipt"); 
			}
	}
		
	//----------------------------------------------------------------
	private void printFileToDefaultPrinter(String fileName) throws Exception
	{
		Runtime rt = Runtime.getRuntime();
		String cmd = "notepad /p " + fileName;
		try
		{
			rt.exec(cmd);
		}
		catch (Exception ex)
		{
			throw new Exception("Error in printing receipt"); 				
		}
	}
}
	