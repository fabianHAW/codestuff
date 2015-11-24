package a1;

public class LineareKongruenz {

	public static void main(String[] args) {
		if (args.length < 1)
			usage();
		else {
			LCG l = new LCG(Long.valueOf(args[0]));
			System.out.println("nextValue of " + args[0] + " is: " + l.nextValue());
		}
	}

	private static void usage() {
		System.out.println("usage: java " + LineareKongruenz.class.getCanonicalName() + " <startvalue>");
	}

}
