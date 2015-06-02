package mware_lib;

import java.util.ArrayList;
import java.util.Arrays;

import accessor_one.ObjectAdapterAO;
import accessor_two.ObjectAdapterAT;

/**
 *  *  Verweise zum Entwurf:
 * <Entwurfsdokument> : Implementierung der vorgegebenen Methoden in Nr. 3 (d) - mware_lib.
 * <Klassendiagramm> : Implementierung durch vorgegebene Methoden in mware_lib - RequestDemultiplexer
 * <Sequenzdiagramm vsp3_sequ_server> : Realiserung der Sequenznummer  1.1.1
 * 
 * @author Fabian
 * 
 *         Teilt die Nachricht dem zugeordneten ObjektAdapter zu.
 */
public class RequestDemultiplexer {
	ArrayList<ObjectAdapter> adapter;

	public RequestDemultiplexer() {
		CommunicationModule.debugPrint(this.getClass(), "initialized");
		this.adapter = new ArrayList<ObjectAdapter>(Arrays.asList(
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
				/*
				 * vsp3_sequ_server: 1.1.1: Skeleton initialisieren
				 */
				item.initSkeleton(m);
				CommunicationModule.debugPrint(this.getClass(),
						"found right skeleton and initialized");
				break;
			}
		}
	}

}
