/**
 * 
 */
package com.example.nl.paymentsengine.services.dto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Shweta Nadkarni
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
"intitiatingPartyAccountNumber",
"counterPartyAccountNumber",
"counterPartyName",
"paymentReference",
"amount",
"paymentDescription"
})
@XmlRootElement
public class PaymentInstruction {

	private String intitiatingPartyAccountNumber;
	
	private String counterPartyAccountNumber;
	
	private String counterPartyName;
	
	private String paymentReference;
	
	private float amount;
	
	private String paymentDescription;

	public String getIntitiatingPartyAccountNumber() {
		return intitiatingPartyAccountNumber;
	}

	public void setIntitiatingPartyAccountNumber(String intitiatingPartyAccountNumber) {
		this.intitiatingPartyAccountNumber = intitiatingPartyAccountNumber;
	}

	public String getCounterPartyAccountNumber() {
		return counterPartyAccountNumber;
	}

	public void setCounterPartyAccountNumber(String counterPartyAccountNumber) {
		this.counterPartyAccountNumber = counterPartyAccountNumber;
	}

	public String getCounterPartyName() {
		return counterPartyName;
	}

	public void setCounterPartyName(String counterPartyName) {
		this.counterPartyName = counterPartyName;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}
	
	
	

}
