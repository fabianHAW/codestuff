package accessor_two;

import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassOneImplBase {

	public static final int ID = 3;
	
	public abstract double methodOne(String param1, double param2)
			throws SomeException112;

	public abstract double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304;

	// erzeugt neues Proxy-Objekt
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		boolean inside = ReferenceModule.contains(rawObjectRef);
		ClassOneImplBase remoteObj = null;

		if (inside) {
			remoteObj = (ClassOneImplBase) ReferenceModule
					.getProxy((RemoteObjectRef) rawObjectRef);
		} else {
			remoteObj = new ClassOneImplBaseProxy(
					(RemoteObjectRef) rawObjectRef);
			ReferenceModule.add((RemoteObjectRef) rawObjectRef, remoteObj);
		}

		return remoteObj;
	}

}
