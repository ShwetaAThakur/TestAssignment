/**
 * This is Implementation class for AccountService interface
 */
package com.example.nl.paymentsengine.services.implementation;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;

import org.apache.commons.lang.StringUtils;

import com.example.nl.paymentsengine.services.constants.AccountServiceExceptionConstants;
import com.example.nl.paymentsengine.services.constants.AccountServiceLogConstants;
import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.dto.Accounts;
import com.example.nl.paymentsengine.services.dto.ReadAccountInput;
import com.example.nl.paymentsengine.services.errorhandlers.AccountServiceException;
import com.example.nl.paymentsengine.services.errorhandlers.JSONFileOperationUtilsException;
import com.example.nl.paymentsengine.services.interfaces.AccountService;
import com.example.nl.paymentsengine.services.util.IBANGenerator;
import com.example.nl.paymentsengine.services.util.JSONFileOperationUtils;


/**
 * @author Shweta Nadkarni
 *
 */

public class AccountServiceImpl implements AccountService {

	
	
	
	/**
	 * Log instance
	 */
	private static final Logger LOGGER = Logger.getLogger( AccountServiceImpl.class.getName() );
	
	
	private static final String DATA_STORAGE_FILE_NAME = "AccountPersistantData.json";
	
	//@Inject
	JSONFileOperationUtils jsonUtils = getJSONUtilsInstance();
	//@Inject
	IBANGenerator iBANGenerator;
	
	/* (non-Javadoc)
	 * @see com.example.nl.paymentsengine.services.interfaces.AccountService#createAccount(com.example.nl.paymentsengine.services.dto.Account)
	 */
	@Override
	public String createAccount(Account inputAccountDetails) throws AccountServiceException {
		
		String accountNumber;
		Account newAccountHolder;
		Accounts accountHolderList;
		try {
			
			//Validate Mandatory input Parameters are available in the Input
			if(inputAccountDetails == null ) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NULL_PARAMETER_EXCEPTION, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			//Account Holder Name is an Mandatory attribute
			if(StringUtils.isBlank(inputAccountDetails.getHolderName())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NO_ACCOUNT_HOLDER_NAME_FOUND, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			//Check IF Account Holder exists in the System. 
			//Currently check is done Only on account holder name
			ArrayList<Account> existingAccountHolders = retrieveAllAccounts();
			if(isAnExistingAccount(existingAccountHolders,inputAccountDetails.getHolderName())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.ACCOUNT_HOLDER_ALREADY_EXISTS, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			
			//Generate New IBAN Number 
			iBANGenerator = getIBANGeneratorInstance();
			//Following loop takes care that same IBAN is not generated again.
			do {
				accountNumber = iBANGenerator.generateIBAN();
			}while(isAnExistingAccountNumber(existingAccountHolders, accountNumber));
			
			newAccountHolder = new Account();
			newAccountHolder.setAccountNumber(accountNumber);
			newAccountHolder.setHolderName(inputAccountDetails.getHolderName());
			newAccountHolder.setBalance(inputAccountDetails.getBalance());
			
			//Add Account holder 
			if(existingAccountHolders == null){
				existingAccountHolders = new ArrayList<Account>();
				
			}
			existingAccountHolders.add(newAccountHolder);
			//Save data 
			accountHolderList =  new Accounts();
			accountHolderList.setAccountList(existingAccountHolders);
			jsonUtils.writeJSONFile(DATA_STORAGE_FILE_NAME, accountHolderList);
			
		}catch (JSONFileOperationUtilsException utilsException) {
			AccountServiceException serviceException =  new AccountServiceException();
			serviceException.setErrorMessages(utilsException.getErrorMessages());
			serviceException.setWarningMessages(utilsException.getWarningMessages());
			serviceException.setInfoMessages(utilsException.getInfoMessages());
			throw serviceException;
		}finally {
			
		}
		return accountNumber;
	}

	
	/* (non-Javadoc)
	 * @see com.example.nl.paymentsengine.services.interfaces.AccountService#readAccount(com.example.nl.paymentsengine.services.dto.ReadAccountInput)
	 */
	@Override
	public Account readAccount(ReadAccountInput readAccountInput) throws AccountServiceException {
		
		Account accountDetails = null;
		
		try {
			//Validate Input
			if(readAccountInput == null ) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NULL_PARAMETER_EXCEPTION, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			if(StringUtils.isBlank(readAccountInput.getAccountNumber())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NO_ACCOUNT_NO_FOUND, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			
			
			Accounts accountDetailsList = (Accounts)jsonUtils.readJSONFile(DATA_STORAGE_FILE_NAME,Accounts.class);
			if (accountDetailsList != null && accountDetailsList.getAccountList() != null && !accountDetailsList.getAccountList().isEmpty()) {
				for (Account tempAccountDetails : accountDetailsList.getAccountList()) {
					if (tempAccountDetails != null
							&& readAccountInput.getAccountNumber().equalsIgnoreCase(tempAccountDetails.getAccountNumber())) {
						accountDetails = tempAccountDetails;
						break;
					}
				}
			}
			
		}catch (JSONFileOperationUtilsException utilsException) {
			AccountServiceException serviceException =  new AccountServiceException();
			serviceException.setErrorMessages(utilsException.getErrorMessages());
			serviceException.setWarningMessages(utilsException.getWarningMessages());
			serviceException.setInfoMessages(utilsException.getInfoMessages());
			throw serviceException;
		}
		return accountDetails;
	}

	/* (non-Javadoc)
	 * @see com.example.nl.paymentsengine.services.interfaces.AccountService#retrieveAllAccounts()
	 */
	@Override
	public ArrayList<Account> retrieveAllAccounts() throws AccountServiceException {

		ArrayList<Account> accountList;
		
		try {
						
			Accounts accountDetailsList = (Accounts)jsonUtils.readJSONFile(DATA_STORAGE_FILE_NAME,Accounts.class);
			accountList = (ArrayList<Account>)accountDetailsList.getAccountList();
		}catch (JSONFileOperationUtilsException utilsException) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.IO_EXCEPTION_IN_READ, utilsException);
			AccountServiceException serviceException =  new AccountServiceException();
			serviceException.setErrorMessages(utilsException.getErrorMessages());
			serviceException.setWarningMessages(utilsException.getWarningMessages());
			serviceException.setInfoMessages(utilsException.getInfoMessages());
			throw serviceException;
		}finally {
			
		}
		return accountList;
	
	}
	
	/**
	 * This method checks whether given Account holder already exists in the system
	 * @param existingAccountHolders list of Account holders available in persistant data storage
	 * @param newAccountHolderName name of AccountHolder which needs to be created.
	 * @return boolean indicator
	 */
	private boolean isAnExistingAccount(ArrayList<Account> existingAccountHolders, String newAccountHolderName) {
		
		boolean isAnExistingHolder = false;
		if (existingAccountHolders != null && !existingAccountHolders.isEmpty()) {
			for(Account tempAccount : existingAccountHolders){
				if(tempAccount !=null && !StringUtils.isBlank(tempAccount.getHolderName()) && tempAccount.getHolderName().trim().equalsIgnoreCase(newAccountHolderName)) {
					isAnExistingHolder = true;
					break;
				}
			}
		}
		return isAnExistingHolder;
	}
	
	/**
	 * This method checks whether an input Account Number already exists in the system
	 * @param existingAccountHolders list of Account holders available in persistant data storage
	 * @param newAccountNumber newly generated account number which needs to be checked.
	 * @return boolean indicator.
	 */
	private boolean isAnExistingAccountNumber(ArrayList<Account> existingAccountHolders, String newAccountNumber) {
		
		boolean isAnExistingAccNo = false;
		if (existingAccountHolders != null && !existingAccountHolders.isEmpty()) {
			for(Account tempAccount : existingAccountHolders){
				if(tempAccount !=null && !StringUtils.isBlank(tempAccount.getAccountNumber()) && tempAccount.getAccountNumber().trim().equalsIgnoreCase(newAccountNumber)) {
					isAnExistingAccNo = true;
					break;
				}
			}
		}
		return isAnExistingAccNo;
	}
	
	/**
	 * This method creates instance of AccountServiceException with appropriate error message key
	 * @param messageId error /info/warning message ID
	 * @param messageType type of error.
	 * @return instance of AccountServiceException
	 */
	private AccountServiceException createExceptionInstance(String messageId,String messageType) {
		AccountServiceException serviceException =  new AccountServiceException();
		ArrayList<String> messageKeys  =  new ArrayList<>();
		messageKeys.add(messageId);
		switch(messageType){
		default:
		case AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR :
			serviceException.setErrorMessages(messageKeys);
			break;
		case AccountServiceExceptionConstants.MESSAGE_TYPE_INFO :
			serviceException.setInfoMessages(messageKeys);
			break;
		case AccountServiceExceptionConstants.MESSAGE_TYPE_WARNING :
			serviceException.setWarningMessages(messageKeys);
			
		
			serviceException.setErrorMessages(messageKeys);
		}
		return serviceException;
	}
	
	/**
	 * This method returns instance for IBANGenerator
	 * @return instance of IBANGenerator
	 */
	protected IBANGenerator getIBANGeneratorInstance() {
		return new IBANGenerator();
	}

	/**
	 * This method returns instance for IBANGenerator
	 * @return instance of IBANGenerator
	 */
	protected JSONFileOperationUtils getJSONUtilsInstance() {
		return new JSONFileOperationUtils();
	}
}
