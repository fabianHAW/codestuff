package accessor_one;

import mware_lib.CommunicationModule;
import mware_lib.ReferenceModule;
import mware_lib.RemoteObjectRef;

public abstract class ClassOneImplBase {

	public static final int ID = 1;
	
	public abstract String methodOne(String param1, int param2) throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		if(!ReferenceModule.contains(rawObjectRef)){
		ClassOneImplBaseProxy proxy = new ClassOneImplBaseProxy((RemoteObjectRef)rawObjectRef);
		ReferenceModule.add((RemoteObjectRef)rawObjectRef, proxy);
		CommunicationModule
		.debugPrint("accessor_one.ClassOneImpleBase: new proxy created");
		return proxy;
		}else{
			CommunicationModule
			.debugPrint("accessor_one.ClassOneImpleBase: got proxy from ReferenceModule");
			return (ClassOneImplBase) ReferenceModule.getProxy((RemoteObjectRef)rawObjectRef);
		}
		
	}

}
