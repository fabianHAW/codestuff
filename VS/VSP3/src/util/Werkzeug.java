package util; 	

//----- Error printing -----------------------------------------------
//----- accessor_one -----------

public class Werkzeug{
	public static void printError(String packageAndClassName,
			String remotetObjName, String methodName, String param1, int param2,
			String exceptionPackageAndClassName, String message) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		
		if (param1 == null) {
			System.out.println("param1 = " + param1);
		} else {
			System.out.println("param1 = \"" + param1 + "\"");
		}
		System.out.println("param2 = " + param2);
		
		System.out.println(exceptionPackageAndClassName + " with message '" + message + "'");
		System.out.println("--------------------------------------------------------------");
	}

	public static void printError(String packageAndClassName,
			String remotetObjName, String methodName, double param1,
			String exceptionPackageAndClassName, String message) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		System.out.println("param1 = " + param1);
		System.out.println(exceptionPackageAndClassName + " with message '" + message + "'");
		System.out.println("--------------------------------------------------------------");
	}
	
	public static void printError(String packageAndClassName,
			String remotetObjName, String methodName,
			String exceptionPackageAndClassName, String message) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		System.out.println(exceptionPackageAndClassName + " with message '" + message + "'");
		System.out.println("--------------------------------------------------------------");	
	}

	//----- accessor_two -----------

	public static void printError(String packageAndClassName,
			String remotetObjName, String methodName, String param1, double param2,
			String exceptionPackageAndClassName, String message) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);

		if (param1 == null) {
			System.out.println("param1 = " + param1);
		} else {
			System.out.println("param1 = \"" + param1 + "\"");
		}
		System.out.println("param2 = " + param2);

		System.out.println(exceptionPackageAndClassName + " with message '" + message + "'");
		System.out.println("--------------------------------------------------------------");
	}


	//----- Result printing --------------------------------------------------------------
	
	//----- accessor_one -----------

	public static void printResult(String packageAndClassName,
			String remotetObjName, String methodName, String param1, int param2,
			String result) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		
		if (param1 == null) {
			System.out.println("param1 = " + param1);
		} else {
			System.out.println("param1 = \"" + param1 + "\"");
		}
		System.out.println("param2 = " + param2);

		System.out.println("Return value = '" + result+"'");
		System.out.println("--------------------------------------------------------------");
	}

	public static void printResult(String packageAndClassName,
			String remotetObjName, String methodName, double param1,
			int result) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		System.out.println("param1 = " + param1);
		System.out.println("Return value = " + result);
		System.out.println("--------------------------------------------------------------");
	}

	public static void printResult(String packageAndClassName,
			String remotetObjName, String methodName,
			double result) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		System.out.println("Return value = " + result);
		System.out.println("--------------------------------------------------------------");
	}

	//----- accessor_two -----------

	public static void printResult(String packageAndClassName,
			String remotetObjName, String methodName, String param1, double param2,
			double result) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(packageAndClassName + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		
		if (param1 == null) {
			System.out.println("param1 = " + param1);
		} else {
			System.out.println("param1 = \"" + param1 + "\"");
		}
		System.out.println("param2 = " + param2);
		
		System.out.println("Return value = " + result);
		System.out.println("--------------------------------------------------------------");
	}
}
