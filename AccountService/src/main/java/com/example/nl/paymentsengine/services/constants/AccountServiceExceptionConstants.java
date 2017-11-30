/**
 * This class contains message constants which are passed to the consumer during exception scenario
 */
package com.example.nl.paymentsengine.services.constants;

/**
 * @author Shweta Nadkarni
 *
 */
public class AccountServiceExceptionConstants {
	
	public static final String MESSAGE_TYPE_ERROR = "ERROR";
	
	public static final String MESSAGE_TYPE_WARNING = "WARNING";
	
	public static final String MESSAGE_TYPE_INFO = "INFO";
	
	public static final  String TECHNICAL_EXCEPTION = "AS0001";
	
	public static final String NULL_PARAMETER_EXCEPTION = "AS0002";
	
	public static final String NO_ACCOUNT_NO_FOUND = "AS0003";
	
	public static final String NO_ACCOUNT_HOLDER_NAME_FOUND = "AS0004";
	
	public static final String ACCOUNT_HOLDER_ALREADY_EXISTS = "AS0005";
	
	public static final String BOTH_ACCOUNT_NO_SAME = "AS0006";
	
	public static final String INVALID_ACCOUNT_NUMBER = "AS0007";

}
