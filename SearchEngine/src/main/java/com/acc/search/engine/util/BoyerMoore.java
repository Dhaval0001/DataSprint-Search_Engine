package com.acc.search.engine.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***************************************************************
 * Compilation: javac BoyerMoore.java Execution: java BoyerMoore pattern text
 *
 * Reads in two strings, the pattern and the input text, and searches for the
 * pattern in the input text using the bad-character rule part of the
 * Boyer-Moore algorithm. (does not implement the strong good suffix rule)
 *
 * % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad text:
 * abacadabrabracabracadabrabrabracad pattern: abracadabra
 *
 * % java BoyerMoore rab abacadabrabracabracadabrabrabracad text:
 * abacadabrabracabracadabrabrabracad pattern: rab
 *
 * % java BoyerMoore bcara abacadabrabracabracadabrabrabracad text:
 * abacadabrabracabracadabrabrabracad pattern: bcara
 *
 * % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad text:
 * abacadabrabracabracadabrabrabracad pattern: rabrabracad
 *
 * % java BoyerMoore abacad abacadabrabracabracadabrabrabracad text:
 * abacadabrabracabracadabrabrabracad pattern: abacad
 *
 ***************************************************************/

public class BoyerMoore {
	private final int R; // the radix
	private final int[] right; // the bad-character skip array
	private final String pat; // or as a string

	// pattern provided as a string
	public BoyerMoore(String pat) {
		this.R = 100000;
		this.pat = pat;

		// position of rightmost occurrence of c in the pattern
		right = new int[R];
		for (int c = 0; c < R; c++)
			right[c] = -1;
		for (int j = 0; j < pat.length(); j++)
			right[pat.charAt(j)] = j;
	}

	// test client
	public static void main(String[] args) {
		String txtPath = "src/main/resources/static/dat/text/";
		String htmlPath = "src/main/resources/static/dat/html/";

		File txt = new File(txtPath);
		File[] Files = txt.listFiles();
		File html = new File(htmlPath);
		File[] Webs = html.listFiles();

		for (int i = 0; i < Files.length; i++) {
			In in = new In(Files[i]);
			String txt1 = String.join(" ", in.readAllLines());

			String[] patterns = { "food" };

			for (String pattern : patterns) {
				BoyerMoore boyerMoore = new BoyerMoore(pattern);
				List<Integer> offsetsList = boyerMoore.search(txt1);

				if (!offsetsList.isEmpty()) {
					System.out.println("Number of occurrences: " + offsetsList.size());
					System.out.println("Pattern \"" + pattern + "\" found at positions:" + offsetsList);
				} else {
					System.out.println("No occurrence of pattern \"" + pattern + "\" found...");
				}
			}
		}
	}

	// return offset of first match; N if no match
	public List<Integer> search(String txt) {
		int M = pat.length();
		int N = txt.length();
		int skip;
		long total = 0L;
		List<Integer> offsetList = null;
		for (int count = 0; count < 100; count++) {
			offsetList = new ArrayList<>();

			long start = System.nanoTime();
			for (int i = 0; i <= N - M; i += skip) {
				skip = 0;
				for (int j = M - 1; j >= 0; j--) {
					if (pat.charAt(j) != txt.charAt(i + j)) {
						skip = Math.max(1, j - right[txt.charAt(i + j)]);
						break;
					}
				}
				if (skip == 0) {
					offsetList.add(i); // found
					skip = pat.length();
				}
			}
			total += System.nanoTime() - start;
		}
		System.out.println("\nAverage time to find pattern \"" + pat + "\" is: " + (total / 100) + "ns");
		return offsetList; // not found
	}
}