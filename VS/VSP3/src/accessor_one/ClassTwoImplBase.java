package accessor_one;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassTwoImplBase {

	public static final int ID = 2;
	
	public abstract int methodOne(double param1) throws SomeException110;
	public abstract double methodTwo() throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		if(!ReferenceModule.contains(rawObjectRef)){
		ClassTwoImplBaseProxy proxy = new ClassTwoImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		CommunicationModule
		.debugPrint("accessor_one.ClassTwoImpleBase: new proxy created");
		return proxy;
	} else  {
		CommunicationModule
		.debugPrint("accessor_one.ClassTwoImpleBase: got proxy from ReferenceModule");
		return (ClassTwoImplBase) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
	}
	}

}
