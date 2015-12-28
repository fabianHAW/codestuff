package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AbstractBaseGraph;

import datastructs.Edge;
import datastructs.Node;
import datastructs.ObjectCloner;
import datastructs.Vertex;

public class CSolver {

	private Map<String, Integer> solutionMap = new HashMap<String, Integer>();
	
	
	public void solve(UndirectedGraph<Vertex, Edge> graphOrig){		
//		Vertex assumptionVertex = null;
//		int assumptionValue = 0;
//		List<Integer> assumptionValueList = null;
//		int vertexCounter = 0;
//		
//		List<Vertex> vList = new ArrayList<Vertex>(graphOrig.vertexSet());
//		Collections.sort(vList);
//
//		assumptionVertex = vList.get(vertexCounter);
//		assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
//		assumptionValue = assumptionValueList.get(0);
//		assumptionValueList.remove(0);
//		vertexCounter++;
		int counter = 0;
		Node n = new Node(graphOrig, counter);
		counter++;
		solve(graphOrig, n, counter);
		
	}
	
//	public Map<String, Integer> solve(UndirectedGraph<Vertex, Edge> graphOrig, List<Vertex> vList,
//			Vertex assumptionVertex, List<Integer> assumptionValueList, Integer assumptionValue, int vertexCounter){
	public boolean solve(UndirectedGraph<Vertex, Edge> graphOrig, Node n, int counter){
		if(counter > graphOrig.vertexSet().size())
			return true;
		
//		for (int i = 0; i < n.getAssumptionValueList().size(); i++){
			System.out.println("****EBENE: " + (n.getDepth()) + "*****");
			System.out.println("preperation: " + "val: " + n.getAssumptionValue() + " vertex: " + n.getAssumptionVertex() + " list: " + n.getAssumptionValueList());
			AC3_LA ac3_la_alg = new AC3_LA(graphOrig);
			UndirectedGraph<Vertex, Edge> graphCopy = getGraphCopy(graphOrig);
			boolean consistent =  ac3_la_alg.ac3_la_procedure(n.getAssumptionVertex(), n.getAssumptionValue());
			
			boolean checked = check(graphOrig, graphCopy, n, consistent, counter);
//			if(!checked)
//				break;
				
			return checked;
			
//		}
		
		
	}
	
	private boolean check(UndirectedGraph<Vertex, Edge> graphOrig, UndirectedGraph<Vertex, Edge> graphCopy, Node n, boolean isConsistent, int counter){
	
		if(isConsistent){
			solutionMap.put(n.getAssumptionVertex().getName(), n.getAssumptionValue());
			Node nNew = new Node(graphOrig, counter);
			
//			if(nNew.getAssumptionValueList().size() == 0)
//				return false;
			
			counter++;
			
			boolean solved = solve(graphOrig, nNew, counter);
			
			//wurde im unteren Bereich des Baumes eine Inkosistenz entdeckt, muss 
			//Domaene im aktuellen Knoten weiter ausgeschoepft werden
			if(!solved){
				solutionMap.remove(n.getAssumptionVertex().getName(), n.getAssumptionValue());
				counter--;
			}

			while(!solved){
				//gibt es weitere Werte in der Domaene des aktuellen Knotens?
				int size = n.getAssumptionValueList().size();
//				if(size == 0)
//					return false;
//				else{
					for(int i = 0; i < size; i++){
						n.setAssumptionValue();
						solved = solve(graphCopy, n, counter);
						if(solved)
							return true;
					}
					return false;
//				}
			}
			
//			return true;
		}else{
			if(n.setAssumptionValue()){
				return solve(graphCopy, n, counter);
			}else{
				return false;
			}
		}
		return true;
	}
	
//	private boolean isConsistent(UndirectedGraph<Vertex, Edge> graphOrig, Vertex assumptionVertex, int assumptionValue){
//		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);
//		return ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue);
//	}
	
	private UndirectedGraph<Vertex, Edge> getGraphCopy(UndirectedGraph<Vertex, Edge> graphOrig){
		UndirectedGraph<Vertex, Edge> graphCopy = null;
		
		try {
			graphCopy = (UndirectedGraph<Vertex, Edge>) ObjectCloner.deepClone(graphOrig);
		} catch (Exception e) {
			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
		}
		return graphCopy;
	}
	
//		System.out.println("preperation: " + "val: " + assumptionValue + " vertex: " +assumptionVertex + " list: " + assumptionValueList);
//		
//		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);
//		
//		UndirectedGraph<Vertex, Edge> graphCopy = null;
//
//		try {
//			graphCopy = (UndirectedGraph<Vertex, Edge>) ObjectCloner.deepClone(graphOrig);
//		} catch (Exception e) {
//			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
//		}
//		
//		System.out.println("graph equals? " + graphCopy.equals(graphOrig));
//		
//		if (graphCopy != null) {
//			System.out.println("#########################\n"
//					+ "Annahmeknoten: " + assumptionVertex.getName() + " Annahmewert: " + assumptionValue 
//					+ "\n##########################");
//			
//			solve();
//			
//			boolean consistent = ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue);
//			
//			if(consistent){
//				//Abbruchbedingung: letzter Knoten erreicht und konsistent?
//				if (vertexCounter == graphOrig.vertexSet().size())
//					return solutionMap;
//				else{
//					System.out.println("*****Annahme war korrekt, naechsten Knoten betrachten****");
//					
//					List<Vertex> vListNeu = new ArrayList<Vertex>(graphOrig.vertexSet());
//					Collections.sort(vListNeu);
//					
////					System.out.println(vListNeu);
//					
//					assumptionVertex = vListNeu.get(vertexCounter);
//					assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
//					vertexCounter++;
//					
//					//Domaene nicht leer
//					if(!assumptionValueList.isEmpty()){
//						assumptionValue = assumptionValueList.get(0);
//						assumptionValueList.remove(0);
//						
//						Map<String, Integer> temp = solve(graphOrig, vList, assumptionVertex, assumptionValueList, assumptionValue, vertexCounter);
//						//Ebene darunter hat eine Loesung gefunden
//						if(temp != null)
//							solutionMap.putAll(temp);
//						//Ebene darunter hat keine Loesung gefunden -> ggf. neue Annahme bei gleichem Knoten
//						else{
//							//Domaene nicht leer
//							if(!assumptionValueList.isEmpty()){
//								assumptionValue = assumptionValueList.get(0);
//								assumptionValueList.remove(0);
//								//Domaene leer
//							}else
//								return null;
//						}
//					}else{
//						//Domaene leer
//						return null;
//					}
//				}
//				
//			} else{
//				
//			}
//			
//			/*
//			 * if-else und ac3 aufrufen -> 
//			 * true: naechste annahme treffen und rekursiv durchgehen bis letzter knoten erreicht wurde
//			 * false: gibt es fuer aktuellen knoten eine weitere moegliche annahme? -> graphCopy noetig 
//			 * 	  ja: wert nehmen und rekursiv weiterarbeiten
//			 *  nein: eine ebene hoeher gehen und dort eine neue annahme treffen, sofern domaene nicht leer ist
//			 */
//			
//		} else
//			return null;
//		
//		
//		return solutionMap;
	

	
	
//	public Map<String, Integer> solve(UndirectedGraph<Vertex, Edge> graphOrig, List<Vertex> vList,
//			Vertex assumptionVertex, List<Integer> assumptionValueList, Integer assumptionValue, int vertexCounter) {
//		AC3_LA ac3_la_alg = new AC3_LA(graphOrig);
//
//
//		UndirectedGraph<Vertex, Edge> graphCopy = null;
//
//		try {
//			graphCopy = (UndirectedGraph<Vertex, Edge>) ObjectCloner.deepClone(graphOrig);
//		} catch (Exception e) {
//			System.out.println("Beim kopieren des Graphen trat ein Fehler auf: " + e);
//		}
//
//		if (graphCopy != null) {
//			System.out.println("#########################\n"
//					+ "Annahmeknoten: " + assumptionVertex.getName() + " Annahmewert: " + assumptionValue 
//					+ "\n##########################");
//			
//			/*
//			 * if-else und ac3 aufrufen -> 
//			 * true: naechste annahme treffen und rekursiv durchgehen bis letzter knoten erreicht wurde
//			 * false: gibt es fuer aktuellen knoten eine weitere moegliche annahme? -> graphCopy noetig 
//			 * 	  ja: wert nehmen und rekursiv weiterarbeiten
//			 *  nein: eine ebene hoeher gehen und dort eine neue annahme treffen, sofern domaene nicht leer ist
//			 */
//			
//		} else
//			return null;
//
//		return solutionMap;
//	}
	
//	if (ac3_la_alg.ac3_la_procedure(assumptionVertex, assumptionValue)) {
//		solutionMap.put(assumptionVertex.getName(), assumptionValue);
//
//		if (vertexCounter >= graphOrig.vertexSet().size())
//			return solutionMap;
//
//		List<Vertex> vListNeu = new ArrayList<Vertex>(graphOrig.vertexSet());
//		Collections.sort(vListNeu);
//		System.out.println(vListNeu);
//
//		// war die Annahme in Ordnung, wird der naechste Knoten mit
//		// einem zur Verfuegung stehenden Wert gewaehlt
//		assumptionVertex = vListNeu.get(vertexCounter++);
//		assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
//
//		if (!assumptionValueList.isEmpty()) {
//			assumptionValue = assumptionValueList.get(0);
//			assumptionValueList.remove(0);
//
//			Map<String, Integer> temp = solve(graphOrig, vListNeu, assumptionVertex, assumptionValueList,
//					assumptionValue, vertexCounter);
//			if (temp == null) {
//				if (assumptionValueList.isEmpty()) {
//					return null;
//				} else {
//					assumptionValue = assumptionValueList.get(0);
//					assumptionValueList.remove(0);
//					
//					solutionMap.putAll(solve(graphOrig, vListNeu, assumptionVertex, assumptionValueList,
//							assumptionValue, vertexCounter));
//				}
//			}
//			else
//				solutionMap.putAll(temp);
//			// solutionMap.putAll(
//			// solve(graphOrig, vListNeu, assumptionVertex,
//			// assumptionValueList, assumptionValue, vertexCounter));
//		}
//	} else {
//		// Ist die vorherige Annahme falsch, wird die naechste des
//		// selben Knoten gewaehlt
//
//		if (assumptionValueList.isEmpty())
//			return null;
//		// return solutionMap;
//
//		List<Vertex> vListNeu = new ArrayList<Vertex>(graphCopy.vertexSet());
//		Collections.sort(vListNeu);
//
//		for (Vertex item : vListNeu) {
//			System.out.println(item.getId() + " " + item.getDomain());
//		}
//		assumptionValue = assumptionValueList.get(0);
//		assumptionValueList.remove(0);
//		
//		Map<String, Integer> temp = solve(graphCopy, vListNeu, assumptionVertex, assumptionValueList, assumptionValue,
//				vertexCounter);
//		if(temp != null)
//			solutionMap.putAll(temp);
//		else{
//			if (assumptionValueList.isEmpty()) {
//				return null;
//			} else {
//				assumptionValue = assumptionValueList.get(0);
//				assumptionValueList.remove(0);
//				
//				solutionMap.putAll(solve(graphOrig, vListNeu, assumptionVertex, assumptionValueList,
//						assumptionValue, vertexCounter));
//			}
//		}
////		solutionMap.putAll(solve(graphCopy, vListNeu, assumptionVertex, assumptionValueList, assumptionValue,
////				vertexCounter));
//	}

	public Map<String, Integer> getSolutionMap(){
		return solutionMap;
	}
}
