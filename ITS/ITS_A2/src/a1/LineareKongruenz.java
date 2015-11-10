package a1;

public class LineareKongruenz {

	public static void main(String[] args) {
		long startwert = System.currentTimeMillis();
		LCG l = new LCG(startwert);
		System.out.println("nextValue von " + startwert + " lautet: " + l.nextValue());
	}

}
