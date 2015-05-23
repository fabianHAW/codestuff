package accessor_one;

import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassTwoImplBase {

	public static final int ID = 1;
	
	public abstract int methodOne(double param1) throws SomeException110;
	public abstract double methodTwo() throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		if(!ReferenceModule.contains(rawObjectRef)){
		ClassTwoImplBaseProxy proxy = new ClassTwoImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		return proxy;
	} else  {
		return (ClassTwoImplBase) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
	}
	}

}
