package proxyserver;

public class Test {

	public static void main(String[] args) {
		String test1 = "bla bla"; String[] test1split = test1.split(" ");
		String test2 = "bla"; String[] test2split = test2.split(" ");
		
		System.out.println("" + test1split[0] + " " + test1split[1]);
		System.out.println("" + test2split[0]);
		
		System.out.println("true = " + "234".matches("[0-9][0-9]*"));
		System.out.println("true = " + "2".matches("[0-9][0-9]*"));
		System.out.println("true = " + "234234".matches("[0-9][0-9]*"));
		System.out.println("false = " + "234d".matches("[0-9][0-9]*"));
		System.out.println("false = " + "ad234".matches("[0-9][0-9]*"));
		System.out.println("false = " + "2s3d4".matches("[0-9][0-9]*"));
		
		System.out.println("false = " + "".matches("[0-9][0-9]*"));
		System.out.println("false =" + "2 34".matches("[0-9][0-9]*"));
		String test = "asdfasfdfda";
		System.out.print(test);
		System.out.println("bla");
		
		System.out.println("\\");
			
	}

}
