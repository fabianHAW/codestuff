package a1;

public class LineareKongruenz {

	public static void main(String[] args) {
		if (args.length < 2)
			usage();
		else {
			LCG l = new LCG(Long.valueOf(args[0]));
			for (int i = 0; i < Integer.valueOf(args[1]); i++) {
				System.out.println((i + 1) + ". Value of " + args[0] + " is: " + l.nextValue());
			}
		}
	}

	private static void usage() {
		System.out.println(
				"usage: java " + LineareKongruenz.class.getCanonicalName() + " <startvalue>" + " <number of values>");
	}

}
