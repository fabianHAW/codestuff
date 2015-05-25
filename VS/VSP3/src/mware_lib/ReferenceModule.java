package mware_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import accessor_one.ClassOneAO;
import accessor_one.ClassTwoAO;
import accessor_two.ClassOneAT;

/**
 * 
 * @author Francis und Fabian
 * 
 */

public class ReferenceModule {

	private static Map<RemoteObjectRef, Object> mapRemoteServant = new HashMap<RemoteObjectRef, Object>();
	private static Map<RemoteObjectRef, Object> mapRemoteProxy = new HashMap<RemoteObjectRef, Object>();

	public static RemoteObjectRef createNewRemoteRef(Object myObject) {

		int port = -1;
		int objectNumber = -1;
		List<String> interfaces = new ArrayList<String>();
		if (myObject instanceof ClassOneAO) {
			objectNumber = 1;
			interfaces.add("methodOne(String param1, int param2)");
		} else if (myObject instanceof ClassTwoAO) {
			objectNumber = 2;
			interfaces.add("methodOne(double param1)");
			interfaces.add("methodTwo()");
		} else if (myObject instanceof ClassOneAT) {
			objectNumber = 3;
			interfaces.add("methodOne(String param1, double param2)");
			interfaces.add("methodTwo(String param1, double param2)");
		}

		RemoteObjectRef rawObjRef = null;

		rawObjRef = new RemoteObjectRef(CommunicationModule.getLocalHost(),
				port, System.currentTimeMillis(), objectNumber, interfaces);

		servantToTable(rawObjRef, myObject);
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: new RemoteObjectRef created an saved in servantlist.");
		return rawObjRef;
	}

	private static void servantToTable(RemoteObjectRef rawObjRef,
			Object myObject) {
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: save new servant in servantlist.");
		mapRemoteServant.put(rawObjRef, myObject);
	}

	public static boolean contains(Object rawObjRef) {
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: check if proxy is in proxylist.");
		return mapRemoteProxy.containsKey(rawObjRef);
	}

	public static Object getProxy(RemoteObjectRef rawObjRef) {
		for (Entry<RemoteObjectRef, Object> item : mapRemoteProxy.entrySet()) {
			if (item.getKey().equals(rawObjRef)) {
				CommunicationModule
						.debugPrint("mware_lib.ReferenceModule: return proxy from proxylist.");
				return item.getValue();
			}
		}
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: proxy not in proxylist.");
		return null;
	}

	public static void add(RemoteObjectRef rawObj, Object remoteObj) {
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: add new proxy to proxylist.");
		mapRemoteProxy.put(rawObj, remoteObj);
	}

	public static Object getServant(RemoteObjectRef rawObjRef) {
		for (Entry<RemoteObjectRef, Object> item : mapRemoteServant.entrySet()) {
			if (item.getKey().equals(rawObjRef)) {
				CommunicationModule
						.debugPrint("mware_lib.ReferenceModule: get servant from servantlist");
				return item.getValue();
			}
		}
		CommunicationModule
				.debugPrint("mware_lib.ReferenceModule: servant not in servantlist.");
		return null;
	}
}
