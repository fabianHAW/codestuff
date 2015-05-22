package accessor_one;

import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassOneImplBase {

	public static final int ID = 1;
	
	public abstract String methodOne(String param1, int param2) throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		ClassOneImplBaseProxy proxy = new ClassOneImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		return proxy;
	}

}
