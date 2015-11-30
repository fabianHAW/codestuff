package util;

import java.util.HashSet;
import java.util.Set;

public class ModulGenerator {

	/**
	 * Prueft modul auf die Voraussetzungen: 1) inkrement ist zum modul
	 * teilerfremd. 2) Jeder Primfaktor von modul teilt faktor - 1. 3) Wenn
	 * modul durch 4 teilbar ist, dann auch faktor - 1. Sind diese nicht erfüllt
	 * wird ein neues modul gesucht
	 * 
	 * @param modulus
	 * @param multiplier
	 * @param increment
	 * @return
	 */
	public static long pruefeModul(long modulus, long multiplier, long increment) {
		// boolean found = false;
		//
		// do {
		if (ggT(increment, modulus) == 1) {
			System.out.println("increment and modulus are coprime!");
			Set<Long> primfaktoren = getPrimfaktoren(modulus);
			if (pruefePrimfaktorenVonModul(primfaktoren, multiplier)) {
				System.out.println("prime factors from multiplier was checked!");
				if (pruefeModulUndFaktorDurchVier(modulus, multiplier)) {
					System.out.println("modulus and multiplier are divisible by 4!");
					// found = true;
				}
			}
		}

		// if (!found)
		// modulus--;
		// } while (!found ^ modulus == 1);

		return modulus;
	}

	/**
	 * Prueft, ob inkrement teilerfremd zu modul ist
	 * 
	 * @param increment
	 * @param modulus
	 * @return groessten gemeinsamen Teiler von inkrement und modul
	 */
	private static long ggT(long increment, long modulus) {
		long temp;
		while (modulus != 0) {
			temp = increment;
			increment = modulus;
			modulus = temp % modulus;
		}
		return increment;
	}

	/**
	 * Ermittelt alle Primfaktoren von modul
	 * 
	 * @param modulus
	 * @return Menge von Primfaktoren von modul
	 */
	private static Set<Long> getPrimfaktoren(Long modulus) {
		Set<Long> primefactors = new HashSet<Long>();

		for (Long i = 2L; i <= Math.sqrt(modulus); i++) {
			if (modulus % i == 0 && isPrim(i))
				primefactors.add(i);
		}

		if (primefactors.isEmpty())
			primefactors.add(modulus);

		return primefactors;
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
	 * @param primefactors
	 *            Menge der Primfaktoren von modul
	 * @param multiplier
	 * @return true, wenn alle Primfaktoren faktor - 1 teilen, sonst false
	 */
	private static boolean pruefePrimfaktorenVonModul(Set<Long> primefactors, long multiplier) {
		for (Long item : primefactors) {
			if ((multiplier - 1) % item != 0)
				return false;
		}

		return true;
	}

	/**
	 * Prueft, ob modul durch 4 teilbar ist und dann, ob auch faktor - 1 durch 4
	 * teilbar ist
	 * 
	 * @param modulus
	 * @param multiplier
	 * @return true, wenn modul und faktor - 1 durch 4 teilbar sind, sonst false
	 */
	private static boolean pruefeModulUndFaktorDurchVier(long modulus, long multiplier) {
		if (modulus % 4 == 0 && (multiplier - 1) % 4 == 0)
			return true;

		return false;
	}

}
