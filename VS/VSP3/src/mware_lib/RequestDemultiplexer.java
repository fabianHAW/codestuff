package mware_lib;

import java.util.ArrayList;
import java.util.Arrays;

import accessor_one.ObjectAdapterAO;
import accessor_two.ObjectAdapterAT;

public class RequestDemultiplexer {
	ArrayList<ObjectAdapter> adapter;

	public RequestDemultiplexer() {
		CommunicationModule.debugPrint(this.getClass(), "initialized");
		adapter = new ArrayList<ObjectAdapter>(Arrays.asList(
				new ObjectAdapterAO(), new ObjectAdapterAT()));
	}

	public void pass(MessageADT m) {
		int objectNumber = m.getObjectRef().getObjectNumber();
		for (ObjectAdapter item : this.adapter) {
			if (item.getSkeletonIDs().contains(objectNumber)) {
				item.initSkeleton(m);
				CommunicationModule.debugPrint(this.getClass(),
						"found right skeleton and initialized");
				break;
			}
		}
	}

}
