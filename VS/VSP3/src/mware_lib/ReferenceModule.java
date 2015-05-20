package mware_lib;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Francis und Fabian
 *
 */

public class ReferenceModule {

	private static Map<RemoteObjectRef, Object> mapRemoteServant = new HashMap<RemoteObjectRef, Object>();
	private static Map<RemoteObjectRef, Object> mapRemoteProxy = new HashMap<RemoteObjectRef, Object>();
	
	
	public static RemoteObjectRef createNewRemoteRef(Object myObject){
//		RemoteObjectRef rawObjRef = new RemoteObjectRef(InetAddress.getByName("localhost"), port, System.currentTimeMillis(), objectNumer, interfaces);
//		servantToTable(rawObjRef, myObject);
//		return rawObjRef;	
		return null;
	}
	
	private static void servantToTable(RemoteObjectRef rawObjRef, Object myObject){
		mapRemoteServant.put(rawObjRef, myObject);
	}
	
	public static boolean contains(Object rawObjRef){
		return mapRemoteProxy.containsKey(rawObjRef);
	}
	
	
	public static Object getProxy(RemoteObjectRef rawObjRef){
		for(Entry<RemoteObjectRef, Object> item : mapRemoteProxy.entrySet()){
			if(item.getKey().equals(rawObjRef)){
				return item.getValue();
			}
		}
		return null;
	}
	
	public static void add(RemoteObjectRef rawObj, Object remoteObj){
		mapRemoteProxy.put(rawObj, remoteObj);
	}
	
	public static Object getServant(RemoteObjectRef rawObjRef){
		for(Entry<RemoteObjectRef, Object> item : mapRemoteServant.entrySet()){
			if(item.getKey().equals(rawObjRef)){
				return item.getValue();
			}
		}
		return null;
	}
}
