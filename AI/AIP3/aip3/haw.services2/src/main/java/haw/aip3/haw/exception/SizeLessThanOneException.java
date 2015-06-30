package haw.aip3.haw.exception;

public class SizeLessThanOneException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6638584763509008840L;

	public SizeLessThanOneException() {
		// TODO Auto-generated constructor stub
		super("Die Groesse der Collection muss >= 1 sein!");
	}

}
