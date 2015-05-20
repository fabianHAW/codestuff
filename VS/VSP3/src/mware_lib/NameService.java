package mware_lib;

public abstract class NameService {

	public NameService() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void rebind(Object servant, String name);
	public abstract Object resolve(String name);
	//Die restlichen Methoden der Spezifikation noch hinzufügen!

}
