package datastructs;

import java.io.*;

public class ObjectCloner {
	// so that nobody can accidentally create an ObjectCloner object
	private ObjectCloner() {
	}

	/**
	 * This method makes a "deep clone" of any object it is given.
	 */
	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}