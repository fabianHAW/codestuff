package accessor_one;

public abstract class ClassOneImplBase {

	public abstract String methodOne(String param1, int param2) throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		return null;
	}

}
