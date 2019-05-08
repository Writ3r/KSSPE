// specify the package
package utilities;

// system imports
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utilities.GlobalVariables;

// project imports

/** Useful Utilities */
//==============================================================
public class Utilities
{
	//input checks for views. 
	
	//Worker/borrower views
	//-----------------------------------------------------------------------------
	public static boolean checkBannerId(String bannerId)
	{
		if(bannerId.length() == GlobalVariables.BANNERID_LENGTH && bannerId.matches("\\d+"))
			return true;
		else
			return false;
	}
	
	//-----------------------------------------------------------------------------
	public static boolean checkName(String name)
	{
		if(name.length() != 0 && name.length() < GlobalVariables.NAME_LENGTH)
			return true;
		else
			return false;
	}
	
	//---------------------------------------------------------------------------
	public static boolean checkEmail(String email)
	{
		if(email.length() != 0 && email.length() < GlobalVariables.EMAIL_LENGTH)
			return true;
		else
			return false;
	}
	
	//------------------------------------------------------------------------
	public static boolean checkPhone(String PhoneNumber)
	{
		if(PhoneNumber.length() != 0 && PhoneNumber.length() < GlobalVariables.PHONE_LENGTH && 
			Utilities.checkProperPhoneNumber(PhoneNumber))
			return true;
		else
			return false;
	}
	
	//-----------------------------------------------------------------------
	public static boolean checkPassword(String Password)
	{
		if(Password.length() != 0 && Password.length() < GlobalVariables.PASSWORD_LENGTH)
			return true;
		else
			return false;
	}
	
	//Input check for equipment view.
	//------------------------------------------------------------------------
	public static boolean checkEquipmentName(String equipmentName)
	{
		if(equipmentName.length() != 0 && equipmentName.length() < GlobalVariables.EQUIPMENT_NAME_LENGTH)
			return true;
		else
			return false;
	}
	
	//-------------------------------------------------------------------------
	public static boolean checkBarcode(String barcode)
	{
		if(barcode.length() == GlobalVariables.BARCODE_LENGTH && barcode.matches("\\d+"))
			return true;
		else
			return false;
	}
	
	
	//------------------------------------------------------------
	public static boolean checkGoodCount(String goodCount)
	{
		if(goodCount.length() != 0)
		{
			try {
				int num = Integer.parseInt(goodCount);

				if(num >= 0 && num < GlobalVariables.EQUIPMENT_COUNT_LENGTH)
					return true;
				else
					return false;
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
		else
			return false;
	}
	
	//-----------------------------------------------------------
	public static boolean checkFairCount(String fairCount)
	{
		return checkGoodCount(fairCount);
	}
	
	//-----------------------------------------------------------
	public static boolean checkPoorCount(String poorCount)
	{
		return checkGoodCount(poorCount);
	}
	
	//-----------------------------------------------------------
	public static boolean checkNotes(String note)
	{
		if(note.length() < GlobalVariables.NOTE_LENGTH)
			return true;
		else
			return false;
	}
	//----------------------------------------------------------
	
	//category view
	public static boolean checkBarcodePrefix(String barcodePrefix)
	{
		if(barcodePrefix.length() == GlobalVariables.BARCODEPREFIX_LENGTH && barcodePrefix.matches("\\d+"))
			return true;
		else
			return false;
	}
	
	//---------------------------------------------------------
	public static boolean checkIsNumber(String s)
	{
		if(s.matches("\\d+"))
			return true;
		else
			return false;
	}
	
	//---------------------------------------------------------
	public static boolean checkCategoryName(String name)
	{
		if(name.length() != 0 && name.length() < GlobalVariables.CATEGORYNAME_LENGTH)
			return true;
		else
			return false;
	}
	
	//------------------------------------------------------
	public static boolean checkPenalty(String p)
	{
		try
		{
			Double d = Double.parseDouble(p);
			return true;
		}
		catch (NumberFormatException ex)
		{
			return false;
		}
	}

	//----------------------------------------------------------
	public static boolean checkDueDate(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try{	
			if(sdf.parse(date) != null)
			{
				Date currDate = new Date();
				Date dueDate = sdf.parse(date);
				
				if (dueDate.after(currDate))
				{
					return true;
				} 
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(ParseException ex)
		{
			return false;
		}
		
	}
	
	//----------------------------------------------------------
	public static String convertToDefaultDateFormat(Date theDate)
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		String valToReturn = formatter.format(theDate);

		return valToReturn;

	}

	//----------------------------------------------------------
	public static String convertDateStringToDefaultDateFormat(String dateStr)
	{

		Date theDate = validateDateString(dateStr);

		if (theDate == null)
		{
			return null;
		}
		else
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			String valToReturn = formatter.format(theDate);

			return valToReturn;
		}
	}

	//----------------------------------------------------------
	protected static Date validateDateString(String str)
	{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			Date theDate = null;

			try
			{
				theDate = formatter.parse(str);
				return theDate;
			}
			catch (ParseException ex)
			{
				SimpleDateFormat formatter2 =
					new SimpleDateFormat("yyyy-MM-dd");

				try
				{
					theDate = formatter2.parse(str);
					return theDate;
				}
				catch (ParseException ex2)
				{
					SimpleDateFormat formatter3 =
						new SimpleDateFormat("yyyy/MMdd");

					try
					{
						theDate = formatter3.parse(str);
						return theDate;
					}
					catch (ParseException ex3)
					{
						SimpleDateFormat formatter4 =
							new SimpleDateFormat("yyyyMM/dd");

						try
						{
							theDate = formatter4.parse(str);
							return theDate;
						}
						catch (ParseException ex4)
						{
							return null;
						}
					}
				}
			}
	}

	//----------------------------------------------------------
	protected static String mapMonthToString(int month)
	{
		if (month == Calendar.JANUARY)
		{
			return "January";
		}
		else
		if (month == Calendar.FEBRUARY)
		{
			return "February";
		}
		else
		if (month == Calendar.MARCH)
		{
			return "March";
		}
		else
		if (month == Calendar.APRIL)
		{
			return "April";
		}
		else
		if (month == Calendar.MAY)
		{
			return "May";
		}
		else
		if (month == Calendar.JUNE)
		{
			return "June";
		}
		else
		if (month == Calendar.JULY)
		{
			return "July";
		}
		else
		if (month == Calendar.AUGUST)
		{
			return "August";
		}
		else
		if (month == Calendar.SEPTEMBER)
		{
			return "September";
		}
		else
		if (month == Calendar.OCTOBER)
		{
			return "October";
		}
		else
		if (month == Calendar.NOVEMBER)
		{
			return "November";
		}
		else
		if (month == Calendar.DECEMBER)
		{
			return "December";
		}
		
		return "";
	}

	//----------------------------------------------------------
	protected static int mapMonthNameToIndex(String monthName)
	{
		if (monthName.equals("January") == true)
		{
			return Calendar.JANUARY;
		}
		else
		if (monthName.equals("February") == true)
		{
			return Calendar.FEBRUARY;
		}
		else
		if (monthName.equals("March") == true)
		{
			return Calendar.MARCH;
		}
		else
		if (monthName.equals("April") == true)
		{
			return Calendar.APRIL;
		}
		else
		if (monthName.equals("May") == true)
		{
			return Calendar.MAY;
		}
		else
		if (monthName.equals("June") == true)
		{
			return Calendar.JUNE;
		}
		else
		if (monthName.equals("July") == true)
		{
			return Calendar.JULY;
		}
		else
		if (monthName.equals("August") == true)
		{
			return Calendar.AUGUST;
		}
		else
		if (monthName.equals("September") == true)
		{
			return Calendar.SEPTEMBER;
		}
		else
		if (monthName.equals("October") == true)
		{
			return Calendar.OCTOBER;
		}
		else
		if (monthName.equals("November") == true)
		{
			return Calendar.NOVEMBER;
		}
		else
		if (monthName.equals("December") == true)
		{
			return Calendar.DECEMBER;
		}
		
		return -1;
	}
	
	
	//----------------------------------------------------
   	protected static boolean checkProperLetters(String value)
   	{
   		for (int cnt = 0; cnt < value.length(); cnt++)
   		{
   			char ch = value.charAt(cnt);
   			
   			if ((ch >= 'A') && (ch <= 'Z') || (ch >= 'a') && (ch <= 'z'))
   			{
   			}
   			else
   			if ((ch == '-') || (ch == ',') || (ch == '.') || (ch == ' '))
   			{
   			}
   			else
   			{
   				return false;
   			}
   		}
   		
   		return true;
   	}
   	
   	//----------------------------------------------------
   	public static boolean checkProperPhoneNumber(String value)
   	{
   		if ((value == null) || (value.length() < 7))
   		{
   			return false;
   		}
   		
   		for (int cnt = 0; cnt < value.length(); cnt++)
   		{
   			char ch = value.charAt(cnt);
   			
   			if ((ch >= '0') && (ch <= '9'))
   			{
   			}
   			else
   			if ((ch == '-') || (ch == '(') || (ch == ')') || (ch == ' '))
   			{
   			}
   			else
   			{
   				return false;
   			}
   		}
   		
   		return true;
   	}
	
	//----------------------------------------------------------------
	public static String formatPenalty(double penalty)
	{
		return (new DecimalFormat("##0.00")).format(penalty);
	}
	
	// Assume the phone number comes in validated - i.e., it has only digits, (, ) and -
	//----------------------------------------------------------------
	public static String formatUSPhoneNumber(String phoneNum)
	{
		String retVal = "";
		String intermediateNum = "";
		
		// remove all special characters from phone number string
		for (int cnt = 0; cnt < phoneNum.length(); cnt++)
		{
			char ch = phoneNum.charAt(cnt);
   			
   			if ((ch >= '0') && (ch <= '9'))
   			{
				intermediateNum += ch;
   			}
		}
		
		// put the dashes in now
		for (int cnt = 0; cnt < intermediateNum.length(); cnt++)
		{
			char ch = intermediateNum.charAt(cnt);
			
			if ((cnt == 2) || (cnt == 5))
			{
				retVal += ch + "-";
			}
			else
			{
				retVal += ch;
			}
		}
		
		return retVal;
		
	}

}

