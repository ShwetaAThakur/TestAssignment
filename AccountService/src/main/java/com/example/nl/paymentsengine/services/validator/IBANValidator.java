/**
 * 
 */
package com.example.nl.paymentsengine.services.validator;


/**
 * @author Shweta Nadkarni
 *
 */

public class IBANValidator {
	
	
	public boolean validateIBANNumber(String iBANNumber) {
		
		return iBANNumber !=null && iBANNumber.trim().length() > 0?true:false;
	}

}
