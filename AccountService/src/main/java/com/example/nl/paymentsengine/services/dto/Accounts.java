/**
 * 
 */
package com.example.nl.paymentsengine.services.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.example.nl.paymentsengine.services.dto.Account;

/**
 * @author Shweta Nadkarni
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
"accountList"
})
@XmlRootElement
public class Accounts {
	
	
	private List<Account> accountList;

	
	public List<Account> getAccountList() {
		return accountList;
	}

	
	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}

}
