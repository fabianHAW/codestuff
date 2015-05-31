package test;

import accessor_two.ClassOneImplBase;
import accessor_two.SomeException112;
import accessor_two.SomeException304;
import mware_lib.CommunicationModule;

/**
 * 
 * @author Fabian
 * 
 *         Implementiert die eigentliche Methode auf der Serverseite.
 */

public class ClassOneAT extends ClassOneImplBase {

	public ClassOneAT() {
		CommunicationModule.debugPrint(this.getClass(), "Object initialized");
	}

	@Override
	public double methodOne(String param1, double param2)
			throws SomeException112 {
		if (param2 < 2) {
			throw new SomeException112("param2 is less than 2");
		}
		CommunicationModule.debugPrint(this.getClass(), "methodOne called");
		return 2.2;
	}

	@Override
	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		if (param1.equals("the monkey without shoes")) {
			throw new SomeException304("wow nice monkey ;)");
		} else if (param2 > 2) {
			throw new SomeException112("param2 is greater than 2");
		}
		CommunicationModule.debugPrint(this.getClass(), "methodTwo called");
		return 3.3;
	}

}
