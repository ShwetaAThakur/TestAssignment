/**
 * 
 */
package com.example.nl.paymentsengine.services.implementation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.enterprise.inject.Alternative;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.nl.paymentsengine.services.constants.AccountServiceExceptionConstants;
import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.dto.Accounts;
import com.example.nl.paymentsengine.services.dto.ReadAccountInput;
import com.example.nl.paymentsengine.services.errorhandlers.AccountServiceException;
import com.example.nl.paymentsengine.services.errorhandlers.JSONFileOperationUtilsException;
import com.example.nl.paymentsengine.services.implementation.AccountServiceImpl;
import com.example.nl.paymentsengine.services.util.IBANGenerator;
import com.example.nl.paymentsengine.services.util.JSONFileOperationUtils;

/**
 * @author Shweta Nadkarni
 *
 */
@Alternative
public class AccountServiceImplTest extends AccountServiceImpl{

	@InjectMocks
	AccountServiceImplTest accountServiceImplTest;
	
	
	
	@Mock
	JSONFileOperationUtils utils;
	
	@Mock
	IBANGenerator iBANGenerator;
	
	@Override
	protected IBANGenerator getIBANGeneratorInstance() {
		return iBANGenerator;
	}
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test
	public void testCreateAccount_nullInput() {
		try {
			accountServiceImplTest.createAccount(null);
			fail("Expected anexception");
		}catch(AccountServiceException e) {
			assertTrue(e.getErrorMessages() != null && AccountServiceExceptionConstants.NULL_PARAMETER_EXCEPTION.equalsIgnoreCase(e.getErrorMessages().get(0)));
		}
	}

	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test
	public void testCreateAccount_negetive_NoAccountHolderName() {
		try {
			Account testAccountDetails =  new Account();
			accountServiceImplTest.createAccount(testAccountDetails);
			fail("Expected anexception");
		}catch(AccountServiceException e) {
			assertTrue(e.getErrorMessages() != null && AccountServiceExceptionConstants.NO_ACCOUNT_HOLDER_NAME_FOUND.equalsIgnoreCase(e.getErrorMessages().get(0)));
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test
	public void testCreateAccount_negative_existingAccount() {
		try {
			Accounts accountsDetails = createMockAccounts();
			Account testAccountDetails =  new Account();
			testAccountDetails.setHolderName("S.Y Nadkarni");
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(accountsDetails);
			accountServiceImplTest.createAccount(testAccountDetails);
			fail("Expected an exception");
		}catch(AccountServiceException e) {
			assertTrue(e.getErrorMessages() != null && AccountServiceExceptionConstants.ACCOUNT_HOLDER_ALREADY_EXISTS.equalsIgnoreCase(e.getErrorMessages().get(0)));
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test
	public void testCreateAccount_positiveCase() {
		try {
			Accounts mockAccountsDetails = createMockAccounts();
			Account testAccountDetails =  new Account();
			testAccountDetails.setHolderName("T.T Rebels");
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(mockAccountsDetails);
			Mockito.doNothing().when(utils).writeJSONFile(Mockito.anyString(),Mockito.any());
			when(iBANGenerator.generateIBAN()).thenReturn("NL31ABNA01234567");
			String accountNumber = accountServiceImplTest.createAccount(testAccountDetails);
			assertTrue("NL31ABNA01234567".equalsIgnoreCase(accountNumber));;
		}catch(AccountServiceException e) {
			fail("This exception not expected");
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test(expected = AccountServiceException.class)
	public void testCreateAccount_negative_exception() throws AccountServiceException{
		try {
			Accounts mockAccountsDetails = createMockAccounts();
			Account testAccountDetails =  new Account();
			testAccountDetails.setHolderName("T.T Rebels");
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(mockAccountsDetails);
			Mockito.doThrow(new JSONFileOperationUtilsException()).when(utils).writeJSONFile(Mockito.anyString(),Mockito.any());
			when(iBANGenerator.generateIBAN()).thenReturn("NL31ABNA01234567");
			accountServiceImplTest.createAccount(testAccountDetails);
			fail("Exception is expected");
			
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#readAccount(com.example.nl.paymentsengine.services.dto.ReadAccountInput)}.
	 */
	@Test
	public void testReadAccount_NoInput() {
		try {
			accountServiceImplTest.readAccount(null);
			fail("Expected anexception");
		}catch(AccountServiceException e) {
			assertTrue(e.getErrorMessages() != null && AccountServiceExceptionConstants.NULL_PARAMETER_EXCEPTION.equalsIgnoreCase(e.getErrorMessages().get(0)));
		}
	}

	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#readAccount(com.example.nl.paymentsengine.services.dto.ReadAccountInput)}.
	 */
	@Test
	public void testReadAccount_noAccountNumber() {
		try {
			accountServiceImplTest.readAccount(new ReadAccountInput());
			fail("Expected anexception");
		}catch(AccountServiceException e) {
			assertTrue(e.getErrorMessages() != null && AccountServiceExceptionConstants.NO_ACCOUNT_NO_FOUND.equalsIgnoreCase(e.getErrorMessages().get(0)));
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#readAccount(com.example.nl.paymentsengine.services.dto.ReadAccountInput)}.
	 */
	@Test
	public void testReadAccount_positive() {
		try {
			ReadAccountInput readAccountInput = new ReadAccountInput();
			readAccountInput.setAccountNumber("NL41ABNA098718882");
			
			Accounts mockAccountsDetails = createMockAccountsForRead();
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(mockAccountsDetails);
			
			Account accountDetails = accountServiceImplTest.readAccount(readAccountInput);
			assertTrue(accountDetails !=null && "S.Y Nadkarni".equalsIgnoreCase(accountDetails.getHolderName()));
		}catch(AccountServiceException e) {
			fail("This exception not expected");
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#readAccount(com.example.nl.paymentsengine.services.dto.ReadAccountInput)}.
	 */
	@Test
	public void testReadAccount_noRecordsFound() {
		try {
			ReadAccountInput readAccountInput = new ReadAccountInput();
			readAccountInput.setAccountNumber("NL41ABNA09870000");
			
			Accounts mockAccountsDetails = createMockAccountsForRead();
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(mockAccountsDetails);
			
			Account accountDetails = accountServiceImplTest.readAccount(readAccountInput);
			assertTrue(accountDetails == null);
		}catch(AccountServiceException e) {
			fail("This exception not expected");
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#createAccount(com.example.nl.paymentsengine.services.dto.Account)}.
	 */
	@Test(expected = AccountServiceException.class)
	public void testReadAccount_negative_exception() throws AccountServiceException{
		try {
			ReadAccountInput readAccountInput = new ReadAccountInput();
			readAccountInput.setAccountNumber("NL41ABNA098718882");
			
			Mockito.doThrow(new JSONFileOperationUtilsException()).when(utils).readJSONFile(Mockito.anyString(),Mockito.any());
			accountServiceImplTest.readAccount(readAccountInput);
			fail("Exception is expected");
			
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * Test method for {@link com.example.nl.paymentsengine.services.implementation.AccountServiceImpl#retrieveAllAccounts()}.
	 */
	@Test
	public void testRetrieveAllAccounts() {
		try {
			Accounts mockAccountsDetails = createMockAccountsForRead();
			when(utils.readJSONFile(Mockito.anyString(),Mockito.any())).thenReturn(mockAccountsDetails);
			
			ArrayList<Account> allAccountDetails = accountServiceImplTest.retrieveAllAccounts();
			assertTrue(allAccountDetails !=null && allAccountDetails.size() == 2);
		}catch(AccountServiceException e) {
			fail("This exception not expected");
		}catch(JSONFileOperationUtilsException e) {
			fail("This exception not expected");
		}
	}
	
	/**
	 * This method creates test data required for mocking. 
	 * @return
	 */
	private Accounts createMockAccounts() {
		
		Accounts existingAccounts =  new Accounts();
		ArrayList<Account> accountHolderList = new ArrayList<>();
		Account tempAccount1 =  new Account();
		tempAccount1.setHolderName("S.Y Nadkarni");
		tempAccount1.setBalance(6789);
		
		Account tempAccount2 =  new Account();
		tempAccount2.setHolderName("Test Data");
		tempAccount2.setBalance(9999);
		accountHolderList.add(tempAccount1);
		accountHolderList.add(tempAccount2);
		
		existingAccounts.setAccountList(accountHolderList);
		return existingAccounts;
	}
	
	private Accounts createMockAccountsForRead() {
		
		Accounts existingAccounts =  new Accounts();
		ArrayList<Account> accountHolderList = new ArrayList<>();
		Account tempAccount1 =  new Account();
		tempAccount1.setHolderName("S.Y Nadkarni");
		tempAccount1.setBalance(6789);
		tempAccount1.setAccountNumber("NL41ABNA098718882");
		
		Account tempAccount2 =  new Account();
		tempAccount2.setHolderName("Test Data");
		tempAccount2.setBalance(9999);
		tempAccount2.setAccountNumber("NL41ABNA008718790");
		
		accountHolderList.add(tempAccount1);
		accountHolderList.add(tempAccount2);
		
		existingAccounts.setAccountList(accountHolderList);
		return existingAccounts;
	}

}
