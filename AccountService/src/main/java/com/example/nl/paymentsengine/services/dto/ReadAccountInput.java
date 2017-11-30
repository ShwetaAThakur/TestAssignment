/**
 * 
 */
package com.example.nl.paymentsengine.services.dto;


/**
 * This is Data Transfer Object which is used to hold search criteria which can be used to search a particular account
 * @author Shweta Nadkarni
 *
 */


public class ReadAccountInput {
	
	private String accountNumber;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	} 
	
	

}
