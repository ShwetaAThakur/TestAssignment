package com.example.nl.restservices.paymentsengine.services;



import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.nl.paymentsengine.services.dto.Account;
import com.example.nl.paymentsengine.services.dto.PaymentInstruction;
import com.example.nl.paymentsengine.services.dto.PaymentReference;
import com.example.nl.paymentsengine.services.dto.ReadAccountInput;
import com.example.nl.paymentsengine.services.errorhandlers.AccountServiceException;
import com.example.nl.paymentsengine.services.errorhandlers.PaymentServiceException;
import com.example.nl.paymentsengine.services.implementation.AccountServiceImpl;
import com.example.nl.paymentsengine.services.implementation.PaymentServiceImpl;
import com.example.nl.paymentsengine.services.interfaces.AccountService;
import com.example.nl.paymentsengine.services.interfaces.PaymentService;
import com.example.nl.paymentsengine.services.resources.Accounts;
import com.example.nl.paymentsengine.services.resources.Exceptions;


@Path("/")
public class AccountRestService {
	
	
	AccountService accountService;
	
	PaymentService paymentService = new PaymentServiceImpl();
	
	@GET
	@Path("/accounts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveAllAccounts(@Context HttpServletRequest request,
			@Context HttpServletResponse response){

		try { 
			ArrayList<Account> accountList = accountService.retrieveAllAccounts();
			Accounts accountDetails = new Accounts();
			accountDetails.setAccountList(accountList);
			if(accountList == null || accountList.isEmpty()) {
				return Response.status(Status.NOT_FOUND).entity(null).build();
			}
			return Response.status(Status.OK).entity(accountDetails).build();
		}catch(AccountServiceException serviceException) { 
			Exceptions exceptionMessages =  new Exceptions();
			exceptionMessages.setErrorMessages(serviceException.getErrorMessages());
			exceptionMessages.setWarningMessages(serviceException.getWarningMessages());
			exceptionMessages.setInfoMessages(serviceException.getInfoMessages());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exceptionMessages).build();
		}
	}
	@GET
	@Path("/accounts/{accountNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAccountDetails(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("accountNumber") String accountNumber){
		
		try {
			ReadAccountInput readAccountInput =  new ReadAccountInput();
			readAccountInput.setAccountNumber(accountNumber);
			Account accountDetails = accountService.readAccount(readAccountInput);
			if(accountDetails == null) {
				return Response.status(Status.NOT_FOUND).entity(null).build();
			}
			return Response.status(Status.OK).entity(accountDetails).build();
		}catch(AccountServiceException serviceException) { 
			Exceptions exceptionMessages =  new Exceptions();
			exceptionMessages.setErrorMessages(serviceException.getErrorMessages());
			exceptionMessages.setWarningMessages(serviceException.getWarningMessages());
			exceptionMessages.setInfoMessages(serviceException.getInfoMessages());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exceptionMessages).build();
		}
		
		
		
	}
	
	@PUT
	@Path("/account")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAccount(@Context HttpServletRequest request, @Context ServletContext servletContext,Account accountDetails){
		
		String accountNumber =  null;

		try {
			accountNumber = accountService.createAccount(accountDetails);
			return Response.status(Status.OK).entity(accountNumber).build();
		}catch(AccountServiceException serviceException) { 
			Exceptions exceptionMessages =  new Exceptions();
			exceptionMessages.setErrorMessages(serviceException.getErrorMessages());
			exceptionMessages.setWarningMessages(serviceException.getWarningMessages());
			exceptionMessages.setInfoMessages(serviceException.getInfoMessages());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exceptionMessages).build();
		}
		
	}
	
	@POST
	@Path("/transfer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tranferMoney(@Context HttpServletRequest request, @Context ServletContext servletContext,PaymentInstruction paymentInstruction){
		
		PaymentReference paymentRefernce;
		try { 
			paymentRefernce = paymentService.transferMoney(paymentInstruction);
			return Response.status(Status.OK).entity(paymentRefernce).build();
		}catch(PaymentServiceException serviceException) { 
			Exceptions exceptionMessages =  new Exceptions();
			exceptionMessages.setErrorMessages(serviceException.getErrorMessages());
			exceptionMessages.setWarningMessages(serviceException.getWarningMessages());
			exceptionMessages.setInfoMessages(serviceException.getInfoMessages());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exceptionMessages).build();
		}
	}
	
}
