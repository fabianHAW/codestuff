package constsraintnet;

import java.util.ArrayList;

public class Node implements Cloneable{
	
	private String name;
	private int nr;
	private ArrayList<Type> domain;
	
	public Node(String name, ArrayList<Type> domain, int nr){
		this.name = name;
		this.domain = domain;
		this.nr = nr;
	}
	
	public boolean removeElem(Type t){
		return domain.remove(t);
	}

	@Override
	public String toString() {
		return "[" + name + ", domain=" + domain + "]" + System.getProperty("line.separator");
	}

	public ArrayList<Type> getDomain() {
		return domain;
	}

	public void setDomain(ArrayList<Type> domain) {
		this.domain = domain;
	}

	public String getName() {
		return name;
	}

	public int getNr() {
		return nr;
	}
	
	@Override
	public Node clone() throws CloneNotSupportedException{
		Node n = (Node) super.clone();
		ArrayList<Type> l = new ArrayList<Type>();
		for(Type t : domain){
			l.add(t.clone());
		}
		n.setDomain(l);
		n.name = name;
		n.nr = nr;
		
		return n;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nr;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nr != other.nr)
			return false;
		return true;
	}
	
	
}
