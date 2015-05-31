package test;

import static org.junit.Assert.*;
import mware_lib.ReferenceModule;

import org.junit.Test;

import shared_types.RemoteObjectRef;
import accessor_two.ClassOneImplBase;
import accessor_two.SomeException112;
import accessor_two.SomeException304;

public class AccessorTwoTest {

	@Test
	public void createServantAndTestRightBehavior() throws SomeException112,
			SomeException304 {
		ClassOneAT coAT = new ClassOneAT();
		assertEquals(2.2, coAT.methodOne("test", 2.4), 0);
		assertEquals(3.3, coAT.methodTwo("test", 1.4), 0);
	}

	@Test(expected = SomeException112.class)
	public void createServantsAndTestException112() throws SomeException112,
			SomeException304 {
		ClassOneAT coAT = new ClassOneAT();
		assertEquals(2.2, coAT.methodOne("test", 1), 0);
		assertEquals(2.2, coAT.methodTwo("test", 2.1), 0);
	}

	@Test(expected = SomeException304.class)
	public void createServantsAndTestException304() throws SomeException112,
			SomeException304 {
		ClassOneAT coAT = new ClassOneAT();
		assertEquals(2.2, coAT.methodTwo("the monkey without shoes", 1), 0);
	}

	@Test
	public void referenceModuleEqualityTestForServantAndProxy() {
		ClassOneAT coAT = new ClassOneAT();
		RemoteObjectRef remoteObj = ReferenceModule.createNewRemoteRef(coAT);
		assertEquals(ReferenceModule.getServant(remoteObj), coAT);

		ClassOneImplBase coib1 = ClassOneImplBase.narrowCast(remoteObj);
		assertEquals(ReferenceModule.getProxy(remoteObj), coib1);

		ClassOneImplBase coib2 = ClassOneImplBase.narrowCast(remoteObj);
		assertEquals(coib1, coib2);
	}
}
