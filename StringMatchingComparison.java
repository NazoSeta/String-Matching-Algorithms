import java.awt.Desktop;
import java.io.*;
import java.util.*;

//First Programmer  ->  Name: Hasan  	     Surname: Özeren    No: 150121036
//Second Programmer ->  Name: Niyazi Ozan    Surname: Ateþ      No: 150121991
//Third Programmer  ->  Name: Ahmet Arda     Surname: Nalbant   No: 150121004
//Fourth Programmer ->  Name: Umut  	     Surname: Bayar     No: 150120043

/* This program is aimed to create 3 different algorithms that checks whenever a pattern occurrence in a text.
 * We use the Brute Force, Horspool, and Boyer Moore algorithms.
 * While the program starts with one of the algorithms, we keep track of the time, occurrence, and number of comparisons.
 * This program works by asking the user to enter the input .html file and the pattern that it will be searching.
 * As an output we give the time, occurrence and number of comparisons in the console.
 * Also, we highlight the found patterns in the output file which is basically the input file but with highlighted patterns. */

public class StringMatchingComparison {

	public static void main(String[] args) throws Exception {
		
		// We take the input .html and pattern information from the user.
		Scanner tempScan = new Scanner(System.in);
		System.out.print("What is the input file labeled as (example.html): ");
		String fileName = tempScan.nextLine();
		System.out.print("What is the pattern labeled as (BAOBAB): ");
		String pattern = tempScan.nextLine();
		File inputFile = new File(fileName);
		
		// We create multiple input Scanners
		Scanner input = new Scanner(inputFile);
		Scanner input2 = new Scanner(inputFile);
		Scanner input3 = new Scanner(inputFile);
		Scanner mark = new Scanner(inputFile);
		
		// We create output file
		File outputFile = new File("output.html");
		PrintWriter output = new PrintWriter(outputFile);
		
		if (inputFile.exists()) { // If file is found.
		// some important variables.
		long comparisonsBruteForce = 0;
		long occurenceBruteForce = 0;
		long comparisonsHorspool = 0;
		long occurenceHorspool = 0;
		long comparisonsBoyerMoore = 0;
		long occurenceBoyerMoore = 0;
		
		// Brute Force
		/* A Brute Force algorithm solves a problem through exhaustion.
		 * It goes through all possible choices until a solution is found.
		 * The time complexity of a brute force algorithm is often proportional to the input size.
		 * Brute Force algorithms are simple and consistent, but very slow.
		 * It starts from the most left character and checks until a mismatch occurrence.
		 * When a mismatch or a successful match is found we check the index only by 1. */
		long startBruteForce = System.currentTimeMillis();
		while (input.hasNext()) {
			String currentLine = input.nextLine();
			for (int i = 0 ; i < currentLine.length() - pattern.length() + 1 ; i++) {
				boolean testing = true;
				for (int j = 0 ; j < pattern.length() && testing ; j++) {
					comparisonsBruteForce++;
					if (pattern.charAt(j) == currentLine.charAt(i+j)) {
						if (pattern.length() == j + 1) {
							occurenceBruteForce++;
						}
					}
					else {
						testing = false;
					}
				}
			}
		}
		long endBruteForce = System.currentTimeMillis();
			
		// Marking spots
		/* This part of the code highlight the found patterns. */
		while (mark.hasNext()) {
			String currentLine = mark.nextLine();
			output.println(currentLine.replaceAll(pattern, "<mark>" + pattern + "</mark>"));
		}
		
		// Horspool
		/* Horspool is a pattern matching algorithm.
		 * It will compare from the right of the pattern with the text.
		 * The shift amount is based on the Bad Symbol Table.
		 * This shift the amount of the first occurrence of the character.
		 * The character that it search for is the character of the first iteration in the text. */
		long startHorspool = System.currentTimeMillis();
		while (input2.hasNext()) {
			String currentLine = input2.nextLine();
			
			for (int i = pattern.length() - 1 ; i < currentLine.length() ; i++) {
				boolean testing = true;
				for (int j = pattern.length() - 1 ; 0 <= j && testing ; j--) {
					comparisonsHorspool++;
					if (pattern.charAt(j) == currentLine.charAt(i - pattern.length() + j + 1)) {
						if (j == 0) {
							occurenceHorspool++;
						}
					}
					else {
						if (pattern.length() == 1) {
						}
						else {
							int jump = 0;
							for (int k = pattern.length() - 2 ; 0 <= k ; k--) {
								if (currentLine.charAt(i) == pattern.charAt(k)) {
									jump = pattern.length() - k - 1;
									break;
								}
								else {
									jump = pattern.length();
								}
							}
							i = i + jump - 1;
						}
						testing = false;
					}
				}
			}
		}
		long endHorspool = System.currentTimeMillis();
		
		// Bad Symbol Table
		/* The Bad Symbol Table create a table based on whenever a character is inside the pattern or not.
		 * If it is not in the pattern it shift the amount of the length of the pattern.
		 * Otherwise it jumps until the first occurrence of the character from the right that match the text from the index. */
		ArrayList<Character> badArrChar = new ArrayList<>();
		ArrayList<Integer> badArrJump = new ArrayList<>();
		
		for (int i = 0 ; i < pattern.length() - 1 ; i++) {
			boolean test = true;
			for (int j = i + 1 ; j < pattern.length() - 1 ; j++) {
				if (pattern.charAt(i) == pattern.charAt(j)) {
					test = false;
					break;
				}
			}
			if (test) {
				badArrChar.add(pattern.charAt(i));
				badArrJump.add(pattern.length() - i - 1);
			}
		}
		// Here we print out the Bad Symbol Table.
		System.out.println("\nBad Symbol Table:");
		for (int i = 0 ; i < badArrJump.size() ; i++) {
			if (badArrJump.get(i) != 0) {
				System.out.println("Char: " + badArrChar.get(i) + "      Jump: " + badArrJump.get(i));
			}
		}
		System.out.println("Char: others Jump: " + pattern.length());
		System.out.println();
		
		// Good Suffix Table
		/* The Good Suffix Table is created based on the correct found matches.
		 * It chooses the best possible shift based on the number of matches from the right. */
	    int[] table = new int[pattern.length()];
	    int[] suffix = suffixes(pattern);

	    for (int i = 0; i < pattern.length(); i++) {
	        table[i] = pattern.length();
	    }

	    for (int i = pattern.length() - 1; i >= 0; i--) {
	        if (suffix[i] == i + 1) {
	            for (int j = 0; j < pattern.length() - i - 1; j++) {
	                if (table[j] == pattern.length()) {
		                   table[j] = pattern.length() - i - 1;
	                }
	            }
	       }
	    }

		for (int i = 0; i < pattern.length() - 1; i++) {
		    table[pattern.length() - 1 - suffix[i]] = pattern.length() - i - 1;
	    }
	    int[] trueTable = new int[pattern.length()];
	    for (int i = 0 ; i < pattern.length()-1 ; i++) {
	    	trueTable[i] = table[pattern.length()-i-2];
	    }
	    trueTable[pattern.length()-1] = trueTable[pattern.length()-2];
	    
	    // Here we print out the Good Suffix Table.
	    System.out.println("Good Suffix Table:");
	    String tempArr[] = new String[pattern.length()];
	    for (int i = 0 ; i < pattern.length() ; i++) {
	    	tempArr[i] = pattern.substring(pattern.length() - 1 - i) + " ";
	    	for (int j = 0 ; j < pattern.length() - i - 1 ; j++) {
	    		tempArr[i] = tempArr[i] + " ";
	    	}
	    }
	    for (int i = 0 ; i < pattern.length() ; i++) {
	    	System.out.println("Matching part (k = " + (i+1) + "): " + tempArr[i] + " = " + trueTable[i]);
	    }
		System.out.println();
		
		// Boyer Moore
		/* Boyer Moore is a pattern matching algorithm. 
		 * It checks from the right of the pattern and shift based on the maximum value of the bad and good tables.
		 * The Bad Sysmbol Table is used a little different than in the Horspool algorithm.
		 * The Table is the same but this time we check in the text what the bad character to find the jump.
		 * We then do jump amount minus the correct part.
		 * The Good Suffix Table is as explained before.
		 * At last we do Math.max(Math.max(badJump,1),goodJump).
		 * This will find the maximum we can jump to possible find a new match. */
		long startBoyerMoore = System.currentTimeMillis();
		while (input3.hasNext()) {
			String currentLine = input3.nextLine();
			
			for (int i = pattern.length() - 1 ; i < currentLine.length() ; i++) {
				boolean testing = true;
				int count = 0;
				for (int j = pattern.length() - 1 ; 0 <= j && testing ; j--) {
					comparisonsBoyerMoore++;
					count++;
					if (pattern.charAt(j) == currentLine.charAt(i - pattern.length() + j + 1)) {
						if (j == 0) {
							occurenceBoyerMoore++;
						}
					}
					else {
						if (pattern.length() == 1) {
						}
						else {
							int jumpBad = 0;
							int jumpGood = 0;
							int jump = 0;
							for (int k = pattern.length() - 2 ; 0 <= k ; k--) {
								if (currentLine.charAt(i) == pattern.charAt(k)) {
									jumpBad = pattern.length() - k - 1;
									break;
								}
								else {
									jumpBad = pattern.length();
								}
							}
							jumpBad = jumpBad - count + 1;
							jumpBad = Math.max(jumpBad, 1);
							
							if (count != 1) {
								jumpGood =  trueTable[count - 2];
							}
							
							jump = Math.max(jumpBad, jumpGood);
							i = i + jump - 1;
						}
						testing = false;
					}
				}
			}
		}
		long endBoyerMoore = System.currentTimeMillis();
		
		// We print out the the number of comparisons, the occurrence and the time it takes for the algorithm to perform.
		System.out.println("Brute Force:");
		System.out.println("Number of comparisons for Brute Force: " + comparisonsBruteForce);
		System.out.println("Number of occurence for Brute Force:   " + occurenceBruteForce);
		System.out.println("Brute Force time(in miliseconds):      " + (endBruteForce - startBruteForce));
		System.out.println();
		
		System.out.println("Horspool:");
		System.out.println("Number of comparisons for Horspool:    " + comparisonsHorspool);
		System.out.println("Number of occurence for Horspool:      " + occurenceHorspool);
		System.out.println("Horspool time(in miliseconds):         " + (endHorspool - startHorspool));
		System.out.println();
		
		System.out.println("Boyer Moore:");
		System.out.println("Number of comparisons for Boyer Moore: " + comparisonsBoyerMoore);
		System.out.println("Number of occurence for Boyer Moore:   " + occurenceBoyerMoore);
		System.out.println("Boyer Moore time(in miliseconds):      " + (endBoyerMoore - startBoyerMoore));
		
		// We close our scanners in this part.
		tempScan.close();
		input.close();
		input2.close();
		input3.close();
		mark.close();
		Desktop.getDesktop().open(outputFile);
		output.close();
		}
		else {
			System.out.println("File doesn't exist!"); // File not found.
		}
	}
	
	// This function helps us to create the Good Suffix Table.
	public static int[] suffixes(String pattern) {
	    int m = pattern.length();
	    int[] suffix = new int[m];
	    int f = 0, g = m - 1;

	    suffix[m - 1] = m;

	    for (int i = m - 2; i >= 0; i--) {
	        if (i > g && suffix[i + m - 1 - f] < i - g) {
	            suffix[i] = suffix[i + m - 1 - f];
	        } else {
	            if (i < g) {
	                g = i;
	            }
	            f = i;
	            while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f)) {
	                g--;
	            }
	            suffix[i] = f - g;
	        }
	    }

	    return suffix;
	}
	
}
