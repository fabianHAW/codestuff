package test;

import java.util.Random;

import accessor_one.ClassTwoImplBase;
import accessor_one.SomeException110;
import accessor_one.SomeException112;
import mware_lib.CommunicationModule;

/**
 *  *  *  Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) im Package "test".
 * <Klassendiagramm> : Implementierung der Klasse im Package test
 * 
 * Servant Klasse - implementiert die eigentliche Funktionalit�t.
 * @author Francis u. Fabian
 * 
 *
 */
public class ClassTwoAO extends ClassTwoImplBase {

	public ClassTwoAO() {
		// TODO Auto-generated constructor stub
		CommunicationModule.debugPrint(this.getClass(), "Object initialized");
	}

	@Override
	public int methodOne(double param1) throws SomeException110 {
		// TODO Auto-generated method stub
		/*
		 * Fuer Nebenlaeufigkeits-Test wartet diese Methode einen Moment
		 */
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if((param1 / 2) > (Integer.MAX_VALUE / 2)){
			throw new SomeException110("ClassTwoAO-methodOne-SomeException110:::(param1 / 2) > (Integer.MAX_VALUE / 2)");
		}
		return (int)param1*2;
	}

	@Override
	public double methodTwo() throws SomeException112 {
		CommunicationModule.debugPrint(this.getClass(), "methodOne called");
		double max = Double.MAX_VALUE;
		Random rand = new Random();
		double result = 0 + (max * rand.nextDouble());
		if(result % 2 != 0){
			throw new SomeException112("ClassTwoAO-methodTwo-SomeException112:::rand % 2 != 0");
		}
		
		return result;
	}

}
