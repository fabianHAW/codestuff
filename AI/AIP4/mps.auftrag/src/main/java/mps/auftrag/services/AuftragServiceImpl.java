package mps.auftrag.services;

import java.util.Collection;

import mps.auftrag.entities.Auftrag;
import mps.auftrag.policies.AuftragPaymentOptionsPolicy;
import mps.auftrag.repositories.AuftragRepository;
import mps.auftrag.values.PaymentOption;
import mps.auftrag.values.PaymentOptionFactory;
import mps.kunde.entities.Kunde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuftragServiceImpl implements AuftragService {
	
	@Autowired
	private AuftragRepository auftragRepository;
	
	@Autowired
	private AuftragPaymentOptionsPolicy auftragPaymentOptionsPolicy;
	
	@Autowired 
	private PaymentOptionFactory paymentOptionFactory;
	

	@Override
	public Collection<Auftrag> getAuftragByKunde(Kunde k) {
		return auftragRepository.findByKunde(k);
	}

	@Override
	public Auftrag createAutrag() {
		Auftrag a = new Auftrag();
		auftragRepository.save(a);
		return a;
	}

	@Override
	public void save(Auftrag auftrag) {
		auftragRepository.save(auftrag);
	}

	@Override
	public Iterable<PaymentOption> getPaymentOptionsForAuftrag(Auftrag a) {
		return auftragPaymentOptionsPolicy.filterPaymentOptions(a, paymentOptionFactory.getAllPaymentOptions());
	}
}
