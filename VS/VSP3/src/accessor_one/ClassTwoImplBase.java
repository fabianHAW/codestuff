package accessor_one;

public abstract class ClassTwoImplBase {

	public abstract int methodOne(double param1) throws SomeException110;
	public abstract double methodTwo() throws SomeException112;
	
	//erzeugt neues Proxy- oder Skeleton-Objekt
	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		return null;
	}

}
