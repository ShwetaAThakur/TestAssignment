package com.example.nl.paymentsengine.services.interfaces;

import com.example.nl.paymentsengine.services.dto.PaymentInstruction;
import com.example.nl.paymentsengine.services.dto.PaymentReference;
import com.example.nl.paymentsengine.services.errorhandlers.PaymentServiceException;

public interface PaymentService {
	
	
	public PaymentReference transferMoney(PaymentInstruction paymentInstruction) throws PaymentServiceException;

}
