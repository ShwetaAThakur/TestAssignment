/**
 * This is class is responsible for generating New IBAN Number. 
 */
package com.example.nl.paymentsengine.services.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Shweta Nadkarni
 *
 */
public class IBANGenerator {
	
	/**
	 * This is Randon Number generator
	 */
	private Random randomGenerator;
	
    /**
     * This is predefined list of BIC Codes.
     */
    private ArrayList<String> bankCodes;

    /**
     * Default Constructor to initialize Bank Code List
     */
    public IBANGenerator()
    { 
    	bankCodes = new ArrayList<String>();
    	bankCodes.add("NL41ABNA");
    	bankCodes.add("NL44ABNA");
    	bankCodes.add("NL46ABNA");
    	bankCodes.add("NL49ABNA");
    	bankCodes.add("NL82ABNA");
    	bankCodes.add("NL39ABNA");
    	
        randomGenerator = new Random();
    }

    /**
     * This method generates Bank Code. 
     * @return Bank Code 
     */
    private String generateBankCode()
    {
        int index = randomGenerator.nextInt(bankCodes.size());
        return bankCodes.get(index);
        
    }
    
    public String generateIBAN() {
    	
    	Random rnd = new Random();
    	int n = 10000000 + rnd.nextInt(90000000);
    	return (generateBankCode() + n);
    }

}
