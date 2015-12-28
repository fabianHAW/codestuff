package datastructs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.UndirectedGraph;

public class Node {

	private Vertex assumptionVertex = null;
	private int assumptionValue = 0;
	private List<Integer> assumptionValueList = null;
	private int depth = 0;
	// private List<Vertex> vList;

	public Node(UndirectedGraph<Vertex, Edge> graphOrig, int counter) {
		depth = counter;

		List<Vertex> vList = new ArrayList<Vertex>(graphOrig.vertexSet());
		Collections.sort(vList);
		if (depth < vList.size()) {
			assumptionVertex = vList.get(depth);
			assumptionValueList = new ArrayList<Integer>(assumptionVertex.getDomain());
			if (assumptionValueList.size() != 0) {
				assumptionValue = assumptionValueList.get(0);
				assumptionValueList.remove(0);
			}
		}
		
	}

	public Vertex getAssumptionVertex() {
		return assumptionVertex;
	}

	public void setAssumptionVertex(Vertex assumptionVertex) {
		this.assumptionVertex = assumptionVertex;
	}

	public int getAssumptionValue() {
		return assumptionValue;
	}

	public void setAssumptionValue(int assumptionValue) {
		this.assumptionValue = assumptionValue;
	}

	public boolean setAssumptionValue() {
		if (!assumptionValueList.isEmpty()) {
			assumptionValue = assumptionValueList.get(0);
			assumptionValueList.remove(0);
			return true;
		} else
			return false;
	}

	public List<Integer> getAssumptionValueList() {
		return assumptionValueList;
	}

	public void setAssumptionValueList(List<Integer> assumptionValueList) {
		this.assumptionValueList = assumptionValueList;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
