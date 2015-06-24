package mps.auftrag.policies;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.values.PaymentOption;
import mps.auftrag.values.PaymentOptionFactory;
import mps.base.annotations.Policy;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

@Policy
public class AuftragPaymentOptionsPolicy {
	
	@Autowired 
	private PaymentOptionFactory paymentOptionFactory;
	
	public Iterable<PaymentOption> filterPaymentOptions(final Auftrag a, final Iterable<PaymentOption> paymentOptions){
		if(!a.getPosten().isEmpty()) {
			return paymentOptions;
		}
		return Lists.newArrayList(paymentOptionFactory.getPaymentOption("Vorkasse"));
	}
}
