//Project2.java, Code.java
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Project2 {
	//enumeration called Color
	public static enum Color {
		EMPTY, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET
	}

	private static final int SEED = 321;//seed 321 required by the instruction
	public static Random rangen = new Random(SEED);

	private static final int TRIALS = 100000;// trials 100000 times required by the instruction
	public static Scanner scnr = new Scanner(System.in);
	public static Scanner entryScnr;
	
    //allOpts function takes no arguments, returns an ArrayList of Code contains every possible Code
	public static ArrayList<Code> allOpts() {
		ArrayList<Code> rtn = new ArrayList<Code>();
		Color[] allColor = Color.values();
		int colorCount = allColor.length;
		for (int i = 0; i < colorCount; ++i) {
			for (int j = 0; j < colorCount; ++j) {
				for (int k = 0; k < colorCount; ++k) {
					for (int l = 0; l < colorCount; ++l) {
						Color[] payload = new Color[4];
						payload[0] = allColor[i];
						payload[1] = allColor[j];
						payload[2] = allColor[k];
						payload[3] = allColor[l];
						Code code = new Code(payload);
						rtn.add(code);
					}
				}
			}
		}
		return rtn;
	}
	
    // randColors() returns a Code at random for computer to create secret Code solution
	public static Code randColors() {
		Color[] payload = new Color[4];
		Color[] allColor = Color.values();
		for (int i = 0; i < payload.length; ++i) {
			payload[i] = allColor[rangen.nextInt(allColor.length)];
		}
		return new Code(payload);
	}
	
    //allDiff() returns a Code with no repeats and in a randomized order
	public static Code allDiff() {
		Color[] payload = new Color[4];
		Color[] allColor = Color.values();
		ArrayList<Color> colorList = new ArrayList<Color>();
		// put all color to a mutable list
		for (int i = 0; i < allColor.length; ++i) {
			colorList.add(allColor[i]);
		}
		// for each selected color, remove it from list, so it won't be selected at next time
		for (int i = 0; i < payload.length; ++i) {
			int randIndex = rangen.nextInt(colorList.size());
			payload[i] = colorList.get(randIndex);
			colorList.remove(randIndex);
		}
		return new Code(payload);
	}
    
	//allSame() returns a Code in which all 4 slots are the same Color
	public static Code allSame() {
		Color[] payload = new Color[4]; 
		Color[] allColor = Color.values();
		Color selected = allColor[rangen.nextInt(allColor.length)];
		for (int i = 0; i < payload.length; ++i) {
			payload[i] = selected;
		}
		return new Code(payload);
	}
    //twoDubs() returns a Code in which there are 2 pairs of Colors, in a randomized order, but not all four the same
	public static Code twoDubs() {
		int pos1, pos2; // pos1 and pos2 has color1, other two pos has color2
		Color color1, color2;
		Color[] payload = new Color[4];
		Color[] allColor = Color.values();
		do {
			color1 = allColor[rangen.nextInt(allColor.length)];
			color2 = allColor[rangen.nextInt(allColor.length)];
		} while (color1.equals(color2));

		do {
			pos1 = rangen.nextInt(payload.length);
			pos2 = rangen.nextInt(payload.length);
		} while (pos1 == pos2);

		for (int i = 0; i < payload.length; ++i) {  
			if (i == pos1 || i == pos2) {
				payload[i] = color1;
			} else {
				payload[i] = color2;
			}
		}

		return new Code(payload);// 这里先调用空参的构造方法，然后再调用有行参的构造方法
	}
    
	// does input validation for exactly 4 entries
	public static Code inputColors() {
		String inputLine = "";
		boolean success = true;
		Color[] rtn = new Color[4];
		String lookupTable = "EROYGBV";

		do {
			// Reset success flag
			success = true;

			// Prompt input four colors
			do {
				System.out.print("CodeBreaker, enter exactly four colors: ");
				inputLine = Project2.scnr.nextLine();
				if (inputLine.length() == 0) {
					System.out.println("You must make an entry, try again.");
				}
			} while (inputLine.length() == 0);

			int cursor = 0; // rtn's cursor
			entryScnr = new Scanner(inputLine);
			while (entryScnr.hasNext()) {
				String token = entryScnr.next();
				char firstLetter = token.toUpperCase().charAt(0);
				int index = lookupTable.indexOf(firstLetter);

				// if first letter not found in lookup table or more than 4
				// color provided
				if (index == -1 || cursor == 4) {
					success = false;
					break;
				}

				// fill rtn's cursor index with Color
				rtn[cursor++] = Color.values()[index];
			}

			// if less than 4 color provided
			if (cursor != 4 || !success) {
				success = false;
				System.out.println("You must enter exactly four colors, try again.");
			}
			entryScnr.close();
		} while (!success);

		return new Code(rtn);
	}
    
	//does input validation on the number of black & white pegs insuring 0<=sum<=4 and each individual between 0 to 4
	public static int inputPegs() {
		String input = "";
		int black = 0, white = 0;

		do {
			System.out.print("CodeMaker, enter number of black (exact match) pegs: ");
			input = scnr.nextLine();
			entryScnr = new Scanner(input);
			if (!entryScnr.hasNextInt()) {
				continue;
			}
			black = entryScnr.nextInt();
			if (black >= 0 && black <= 4) {
				break;
			} else {
				System.out.println("must enter a value between 0 and 4");
			}
			entryScnr.close();
		} while (true);

		do {
			System.out.print("CodeMaker, enter number of White (wrong position) pegs: ");
			input = scnr.nextLine();
			entryScnr = new Scanner(input);
			if (!entryScnr.hasNextInt()) {
				continue;
			}
			white = entryScnr.nextInt();
			if (white >= 0 && white <= (4 - black)) {
				break;
			} else {
				System.out.println("white between 0 and 4, and 0<=black+white<=4 too");
			}
			entryScnr.close();
		} while (true);

		return black * 10 + white;
	}
	
    //main method
	public static void main(String[] args) {
		String playAgain = "Y";
		int gamesCount = 0, turnsSum = 0;
		String makerName = "", breakerName = "";
		boolean isMakerAI = false, isBreakerAI = false, isHumanPlaying = false;

		do {
			System.out.print("Enter name for the codeMaker: ");
			makerName = scnr.nextLine();
			isMakerAI = makerName.toUpperCase().startsWith("AI");
		} while (makerName.length() == 0);

		do {
			System.out.print("Enter a different name for the codeBreaker: ");
			breakerName = scnr.nextLine();
			isBreakerAI = breakerName.toUpperCase().startsWith("AI");
		} while (breakerName.length() == 0 || makerName.equals(breakerName));

		isHumanPlaying = !isMakerAI || !isBreakerAI;

		while (playAgain.toUpperCase().startsWith("Y") || (!isHumanPlaying && gamesCount < TRIALS)) {
			int turns = 0, peg = 0;

			Code guessing = null;
			Code solution = randColors();

			ArrayList<Code> possibleCodes = allOpts();

			System.out.println("\nPlaying game #" + ++gamesCount);
			boolean isVerbosePrint = isHumanPlaying || gamesCount >= TRIALS;

			if (isMakerAI) {
				if (isVerbosePrint) {
					System.out.println(makerName + " has made a code for " + breakerName + " to break.");
				}
			} else {
				System.out.println(makerName + " should mentally make a code for " + breakerName + " to break.");
			}

			do {
				++turns;
				if (isVerbosePrint) {
					System.out.println("\nThis is game#" + gamesCount + ", turn #" + turns);
				}

				if (isBreakerAI) {
					if (turns == 1) {
						// AI breaker first time guess is twoDubs()
						guessing = allDiff();// TODO: change this as needed
					} else {
						// after first time use pickMove to choice guess
						guessing = guessing.pickMove(possibleCodes, peg);
						if (guessing == null) {
							System.out.println(
									breakerName + " cannot make guess. " + "Inconsistent peg input. Terminating game");
							break;
						}
					}
					if (isVerbosePrint) {
						System.out.println(breakerName + " entered " + guessing);
					}
				} else {
					// Human breaker need input color manually.
					guessing = inputColors();
				}

				if (isMakerAI) {
					// AI maker calculate the pegs
					peg = guessing.pegs(solution);
				} else {
					// Human maker input pegs
					peg = inputPegs();
				}

				if (isVerbosePrint) {
					System.out.println(breakerName + " guessed " + guessing + ", and " + makerName
							+ " declares: Black = " + peg / 10 + ", White = " + peg % 10);
				}

				if (peg == 40) {
					if (isVerbosePrint) {
						System.out.println(breakerName + " broke the code of " + makerName + " on turn#" + turns);
					}
					break;
				} else if (turns >= 10) {
					if (isVerbosePrint) {
						System.out.println(breakerName + " was unable to break the " + "code of " + makerName
								+ " within the ten turn limit");
						if (isMakerAI) {
							System.out.println("The solution was " + solution);
						} else {
							System.out.println("The solution will be verbally revealed by " + makerName + " now");
						}
					}
					break;
				}
			} while (true);

			playAgain = ""; // reset playAgain flag
			if (isVerbosePrint) {
				do {
					System.out.print("Do you wish to play again (Y/N)? ");
					playAgain = scnr.nextLine();
				} while (playAgain.length() == 0);
			}
			turnsSum += turns;
		}
		System.out.println("\nThe average game's turn length was " + (double) turnsSum / gamesCount);
	}
}
