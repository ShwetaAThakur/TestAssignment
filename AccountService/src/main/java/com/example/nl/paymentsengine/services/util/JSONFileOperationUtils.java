/**
 * This is Util class provides operations for reading and writing JSON file which serves as Persistant Data storage for this Assignments. 
 */
package com.example.nl.paymentsengine.services.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


import com.example.nl.paymentsengine.services.constants.AccountServiceExceptionConstants;
import com.example.nl.paymentsengine.services.constants.AccountServiceLogConstants;
import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.errorhandlers.JSONFileOperationUtilsException;

/**
 * @author Shweta Nadkarni
 *
 */


public class JSONFileOperationUtils {
	
	private static final Logger LOGGER = Logger.getLogger( JSONFileOperationUtils.class.getName() );
	/**
	 * This operation is responsible for reading a JSON File which present in classpath. 
	 * File recognized by input parameter jsonFileName
	 * @param jsonFileName Name of the JSON file available in classpath
	 *  @param restResource Class name in which data needs to be bind.
	 * @return  instance of restResource holding data from JSON File.
	 */
	
	public Object readJSONFile(String jsonName, Class restResource) throws JSONFileOperationUtilsException{
		BufferedReader br = null;
		Object obj = null;
		try {
			obj = restResource.newInstance();
			ObjectMapper mapper = new ObjectMapper();
			String sCurrentLine;
			StringBuffer stringBuff = new StringBuffer();
			InputStream in = getClass().getClassLoader().getResourceAsStream(jsonName); 
			br = new BufferedReader(new InputStreamReader(in));
			while ((sCurrentLine = br.readLine()) != null) {
				stringBuff.append(sCurrentLine);
			}
			sCurrentLine = stringBuff.toString();
			obj = mapper.readValue(sCurrentLine, restResource);
			
		} catch (FileNotFoundException e) {
			 LOGGER.log(Level.SEVERE, AccountServiceLogConstants.FILE_NOT_FOUND_EXCEPTION, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.IO_EXCEPTION_IN_READ, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.IO_EXCEPTION_IN_READ, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		} catch (InstantiationException e) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.IO_EXCEPTION_IN_READ, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		}finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				LOGGER.log(Level.SEVERE, AccountServiceLogConstants.IO_EXCEPTION_IN_READ, ex);
			}
		}
		return obj;
	}
	/**
	 * This operation is responsible for writing data in a JSON File which present in classpath. 
	 * File recognized by input parameter jsonFileName
	 * @param jsonFileName Name of the JSON file available in classpath
	 * @param restResource Class which holds the data which is stored in Persistant data storage. 
	 */
	public void writeJSONFile(String jsonFileName, Object restResource) throws JSONFileOperationUtilsException{
		
		String fileName= getClass().getClassLoader().getResource(jsonFileName).getFile(); 
		ObjectMapper mapper = new ObjectMapper();
		try {
			String decodedURL = URLDecoder.decode(fileName, "UTF-8");
			File jsonFile = new File(decodedURL);
			mapper.writeValue(jsonFile, restResource);
		} catch (JsonGenerationException e) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.JSON_GENERATE_EXCEPTION_IN_WRITE, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		} catch (JsonMappingException e) {
			LOGGER.log(Level.SEVERE, AccountServiceLogConstants.JSON_MAPPING_EXCEPTION_IN_WRITE, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,AccountServiceLogConstants.IO_EXCEPTION_IN_WRITE, e);
			JSONFileOperationUtilsException utilException =  new JSONFileOperationUtilsException();
			ArrayList<String> errorMessage  =  new ArrayList<>();
			errorMessage.add(AccountServiceExceptionConstants.TECHNICAL_EXCEPTION);
			utilException.setErrorMessages(errorMessage);
			throw utilException;
		}
	}

}
