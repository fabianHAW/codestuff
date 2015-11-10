package util;

import java.util.HashSet;
import java.util.Set;

public class ModulGenerator {

	/**
	 * Prueft modul auf die Voraussetzungen: 
	 * 1) inkrement ist zum modul teilerfremd. 
	 * 2) Jeder Primfaktor von modul teilt faktor - 1. 
	 * 3) Wenn modul durch 4 teilbar ist, dann auch faktor - 1. Sind diese nicht erfüllt
	 * wird ein neues modul gesucht
	 * 
	 * @param modul
	 * @param faktor
	 * @param inkrement
	 * @return
	 */
	public static long generateModul(long modul, long faktor, long inkrement) {
		boolean gefunden = false;

		do {
			if (ggT(inkrement, modul) == 1) {
				System.out.println("inkrement und modul sind teilerfremd!");
				Set<Long> primfaktoren = getPrimfaktoren(modul);
				if (pruefePrimfaktorenVonModul(primfaktoren, faktor)) {
					System.out.println("Primfaktoren von faktor wurden geprueft!");
					if (pruefeModulUndFaktorDurchVier(modul, faktor)) {
						System.out.println("modul und faktor sind durch 4 teilbar");
						gefunden = true;
					}
				}
			}

			if (!gefunden)
				modul--;
		} while (!gefunden ^ modul == 1);

		return modul;
	}

	/**
	 * Prueft, ob inkrement teilerfremd zu modul ist
	 * 
	 * @param inkrement
	 * @param modul
	 * @return groessten gemeinsamen Teiler von inkrement und modul
	 */
	private static long ggT(long inkrement, long modul) {
		long temp;
		while (modul != 0) {
			temp = inkrement;
			inkrement = modul;
			modul = temp % modul;
		}
		return inkrement;
	}

	/**
	 * Ermittelt alle Primfaktoren von modul
	 * 
	 * @param modul
	 * @return Menge von Primfaktoren von modul
	 */
	private static Set<Long> getPrimfaktoren(Long modul) {
		Set<Long> primfaktoren = new HashSet<Long>();

		for (Long i = 2L; i <= Math.sqrt(modul); i++) {
			if (modul % i == 0 && isPrim(i))
				primfaktoren.add(i);
		}

		if (primfaktoren.isEmpty())
			primfaktoren.add(modul);

		return primfaktoren;
	}

	/**
	 * Prueft, ob Wert eine Primzahl ist
	 * 
	 * @param val
	 * @return true, wenn Primzahl, sonst false
	 */
	private static boolean isPrim(long val) {
		if (val <= 2) {
			return (val == 2);
		}
		for (long i = 2; i * i <= val; i++) {
			if (val % i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Prueft, ob jeder Primfaktor von modul faktor - 1 teilt
	 * 
	 * @param primfaktoren
	 *            Menge der Primfaktoren von modul
	 * @param faktor
	 * @return true, wenn alle Primfaktoren faktor - 1 teilen, sonst false
	 */
	private static boolean pruefePrimfaktorenVonModul(Set<Long> primfaktoren, long faktor) {
		for (Long item : primfaktoren) {
			if ((faktor - 1) % item != 0)
				return false;
		}

		return true;
	}

	/**
	 * Prueft, ob modul durch 4 teilbar ist und dann, ob auch faktor - 1 durch 4
	 * teilbar ist
	 * 
	 * @param modul
	 * @param faktor
	 * @return true, wenn modul und faktor - 1 durch 4 teilbar sind, sonst false
	 */
	private static boolean pruefeModulUndFaktorDurchVier(long modul, long faktor) {
		if (modul % 4 == 0 && (faktor - 1) % 4 == 0)
			return true;

		return false;
	}

}
