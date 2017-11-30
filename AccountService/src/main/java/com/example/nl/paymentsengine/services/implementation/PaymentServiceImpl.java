/**
 * This is an implementation class of PaymentService
 */
package com.example.nl.paymentsengine.services.implementation;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.inject.Default;

import org.apache.commons.lang.StringUtils;

import com.example.nl.paymentsengine.services.constants.AccountServiceExceptionConstants;
import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.dto.Accounts;
import com.example.nl.paymentsengine.services.dto.PaymentInstruction;
import com.example.nl.paymentsengine.services.dto.PaymentReference;
import com.example.nl.paymentsengine.services.errorhandlers.JSONFileOperationUtilsException;
import com.example.nl.paymentsengine.services.errorhandlers.PaymentServiceException;
import com.example.nl.paymentsengine.services.interfaces.PaymentService;
import com.example.nl.paymentsengine.services.util.JSONFileOperationUtils;
import com.example.nl.paymentsengine.services.validator.IBANValidator;

/**
 * @author Shweta Nadkarni
 *
 */

public class PaymentServiceImpl implements PaymentService {

	private static final String DATA_STORAGE_FILE_NAME = "AccountPersistantData.json";
	
	private JSONFileOperationUtils jsonUtils = new JSONFileOperationUtils();
	
	private IBANValidator iBANValidator = new IBANValidator();
	
	
	/* (non-Javadoc)
	 * @see com.example.nl.paymentsengine.services.interfaces.PaymentService#transferMoney(com.example.nl.paymentsengine.services.dto.PaymentInstruction)
	 */
	@Override
	public PaymentReference transferMoney(PaymentInstruction paymentInstruction) throws PaymentServiceException {
		
		Account counterPartyAccount = null;
		Account initiatingPartyAccount = null;
		PaymentReference paymentReference = null;
		Lock l = new ReentrantLock();
		l.lock();
		try {
			paymentReference = new PaymentReference();
			
			//Check whether data exists in the request
			if(paymentInstruction == null) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NULL_PARAMETER_EXCEPTION, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			if(StringUtils.isBlank(paymentInstruction.getCounterPartyAccountNumber()) 
					|| StringUtils.isBlank(paymentInstruction.getIntitiatingPartyAccountNumber())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.NO_ACCOUNT_NO_FOUND, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
					
			//Check if Both account numbers are not same 
			if(!StringUtils.isBlank(paymentInstruction.getCounterPartyAccountNumber()) 
					&& paymentInstruction.getCounterPartyAccountNumber().trim().equalsIgnoreCase(paymentInstruction.getIntitiatingPartyAccountNumber())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.BOTH_ACCOUNT_NO_SAME, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
				
			}
			//Check both account numbers are in correct format
			if(!iBANValidator.validateIBANNumber(paymentInstruction.getCounterPartyAccountNumber())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.INVALID_ACCOUNT_NUMBER, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			if(!iBANValidator.validateIBANNumber(paymentInstruction.getIntitiatingPartyAccountNumber())) {
				throw createExceptionInstance(AccountServiceExceptionConstants.INVALID_ACCOUNT_NUMBER, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
			}
			
			//Generate unique Payment Reference
			paymentReference.setTransactionNumber(generatePaymentReferenceNumber());
			paymentReference.setStaus("INITIATED");
			Accounts accountDetailsList = (Accounts)jsonUtils.readJSONFile(DATA_STORAGE_FILE_NAME,Accounts.class);
			if (accountDetailsList != null && accountDetailsList.getAccountList() != null && !accountDetailsList.getAccountList().isEmpty()) {
				for (Account tempAccountDetails : accountDetailsList.getAccountList()) {
					if (tempAccountDetails != null
							&& paymentInstruction.getCounterPartyAccountNumber().equalsIgnoreCase(tempAccountDetails.getAccountNumber())) {
						counterPartyAccount = tempAccountDetails;
						
					}
					if (tempAccountDetails != null
							&& paymentInstruction.getIntitiatingPartyAccountNumber().equalsIgnoreCase(tempAccountDetails.getAccountNumber())) {
						initiatingPartyAccount = tempAccountDetails;
						
					}
					if(initiatingPartyAccount != null && counterPartyAccount != null) {
						break;
					}
				}
				if(initiatingPartyAccount == null) {
					//No Such Account exists
					throw createExceptionInstance(AccountServiceExceptionConstants.NO_ACCOUNT_NO_FOUND, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
				}
				
				if(counterPartyAccount == null) {
					//No Such Account exists
					throw createExceptionInstance(AccountServiceExceptionConstants.NO_ACCOUNT_NO_FOUND, AccountServiceExceptionConstants.MESSAGE_TYPE_ERROR);
				}
				
				//Check if amount to be transferred is more than daily limit/transaction limit of Initiating Party 
				
				//Check if amount to be transferred is more than current balance of of Initiating Party 
				if(initiatingPartyAccount.getBalance() < paymentInstruction.getAmount()) {
					//Throw exception
				}else {
					counterPartyAccount.setBalance(counterPartyAccount.getBalance() + paymentInstruction.getAmount());
					initiatingPartyAccount.setBalance(initiatingPartyAccount.getBalance() - paymentInstruction.getAmount());
					//jsonUtils.writeJSONFile(DATA_STORAGE_FILE_NAME, counterPartyAccount);
					//jsonUtils.writeJSONFile(DATA_STORAGE_FILE_NAME, initiatingPartyAccount);
					jsonUtils.writeJSONFile(DATA_STORAGE_FILE_NAME, accountDetailsList);
				
				}
				paymentReference.setStaus("SUCCESS");
			
			}
		}catch(JSONFileOperationUtilsException utilsException){
			PaymentServiceException serviceException =  new PaymentServiceException();
			serviceException.setErrorMessages(utilsException.getErrorMessages());
			serviceException.setWarningMessages(utilsException.getWarningMessages());
			serviceException.setInfoMessages(utilsException.getInfoMessages());
			throw serviceException;
		}finally {
			l.unlock();
		}
		return paymentReference;
	}
	
	/**
	 * This Method will return random payment refernce number for each transaction.
	 * @return generated Number
	 */
	private String generatePaymentReferenceNumber() {
		
		Random rnd = new Random();
    	int n = 1000000000 + rnd.nextInt(900000000);
    	return (n+"");
		
	}
	
	/**
	 * This method creates instance of PaymentServiceException with appropriate error message key
	 * @param messageId error /info/warning message ID
	 * @param messageType type of error.
	 * @return instance of PaymentServiceException
	 */
	private PaymentServiceException createExceptionInstance(String messageId,String messageType) {
		PaymentServiceException serviceException =  new PaymentServiceException();
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

}
