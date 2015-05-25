package mware_lib;

import java.util.ArrayList;
import java.util.Arrays;

import accessor_one.ObjectAdapterAO;
import accessor_two.ObjectAdapterAT;

/**
 * 
 * @author Fabian
 * 
 *         Ist fuer die Weiterleitung der MessageADT zum passenden
 *         Objekt-Adapter zustaendig
 */
public class RequestDemultiplexer {
	ArrayList<ObjectAdapter> adapter;

	public RequestDemultiplexer() {
		CommunicationModule.debugPrint(this.getClass(), "initialized");
		adapter = new ArrayList<ObjectAdapter>(Arrays.asList(
				new ObjectAdapterAO(), new ObjectAdapterAT()));
	}

	/**
	 * Sucht aus der Liste der vorhandenen Objekt-Adapter den richtigen aus und
	 * leitet die Anfrage an ihn weiter
	 * 
	 * @param m
	 *            MessageADT die an Objekt-Adapter weitergeleitet wird
	 */
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
