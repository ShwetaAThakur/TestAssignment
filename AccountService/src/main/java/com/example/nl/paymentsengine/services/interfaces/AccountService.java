/**
 * 
 */
package com.example.nl.paymentsengine.services.interfaces;

import java.util.ArrayList;

import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.dto.ReadAccountInput;
import com.example.nl.paymentsengine.services.errorhandlers.AccountServiceException;

/**
 * @author Shweta Nadkarni
 *
 */

public interface AccountService {
	
	/**
	 * This Service is responsible for creating an account with given details into a persistant storage.
	 * @param accountHolderName
	 * @param initialBalance
	 * @return generated account number
	 * @throws AccountServiceException
	 */
	public String createAccount(Account inputAccountDetails) throws AccountServiceException;
	
	/**
	 * This Service is responsible for searching a particular account from Data storage based on search criteria mentioned in the searchAccountInput
	 * @param readAccountInput provides search Criteria
	 * @return Account details
	 * @throws AccountServiceException
	 */
	public Account readAccount(ReadAccountInput readAccountInput) throws AccountServiceException;
	
	/**
	 * This service retrieves all accounts which are present in data storage
	 * @return list of Accounts
	 * @throws AccountServiceException
	 */
	public ArrayList<Account> retrieveAllAccounts()  throws AccountServiceException;

}
