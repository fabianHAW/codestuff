package mps.auftrag.values;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PaymentOptionFactory {
	private final Map<String,PaymentOption> map = new HashMap<>();

	// this should probably go some place else...
	private final PaymentOption[] PAYMENT_OPTION_ARR = {
			getPaymentOption("Kreditkarte"),
			getPaymentOption("Paypal"),
			getPaymentOption("Vorkasse")
	}; 
	
	private final List<PaymentOption> ALL_PAYMENT_OPTIONS = Arrays.asList(PAYMENT_OPTION_ARR);

	public PaymentOption getPaymentOption(String type) {
		synchronized (map) {
			PaymentOption option = map.get(type);
			if(option==null) {
				map.put(type, option=new PaymentOption(type));
			}
			return option;
		}
	}
	
	public List<PaymentOption> getAllPaymentOptions(){
		return ALL_PAYMENT_OPTIONS;
	}
}
