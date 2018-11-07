/**
 * Class Code has only a single field, called slots, which is a regular array of Colors with length 4
 * and has 2 constructors and several methods
 * @author Yifan Peng
 * Date: 07/28/2017
 */
import java.util.ArrayList;

public class Code {
	//declare and initialize array of Color with length 4
	private Project2.Color[] slots = new Project2.Color[4];
    /**
     * default constructor with no parameters which creates a default Code of all Empties
     */
	public Code() {
		// Size of slots is 4 (implicitly)
		this.slots = new Project2.Color[] { Project2.Color.EMPTY, Project2.Color.EMPTY, Project2.Color.EMPTY,
				Project2.Color.EMPTY };
	}
    /**
     * constructor which takes an array of Colors and makes a Code with that argument as the slots
     * @param payload
     */
	public Code(Project2.Color[] payload) {
		this.setSlots(payload);
	}
    /**
     * getter for private field slots
     * @return Project2.Color[]
     */
	public Project2.Color[] getSlots() {
		return this.slots;
	}
    /**
     * setter for private field slots
     * @param payload
     */
	public void setSlots(Project2.Color[] payload) {
		if (payload.length == 4) {
			this.slots = payload;
		}
	}
    /**
     * pegs method take a Code as an argument and returns the quantity of
     * both black and white pegs as a single int
     * 
     * @param solution
     * @return int
     */
	public int pegs(Code solution) {
		int rtn = 0;
		for (int i = 0; i < 4; ++i) {
			if (this.slots[i] == solution.slots[i]) {
				rtn += 10;
			} else {
				for (int j = 0; j < 4; ++j) {
					if (this.slots[i] == solution.slots[j]) {
						++rtn;
						break;
					}
				}
			}
		}
		return rtn;
	}
    /**
     * pickMove method which takes two arguments, an ArrayList of Codes 
     * (representing all the Codes which are consistent with all the peg results so far in the game) 
     * and an int (representing the peg quantities for the calling instance).
     * 
     * @param consistent
     * @param peg
     * @return the code to be the next guess
     */
	public Code pickMove(ArrayList<Code> consistent, int peg) {
		if (consistent == null || consistent.isEmpty()) {
			return null;
		}

		int cursor = 0;
		while (cursor < consistent.size()) {
			if (this.pegs(consistent.get(cursor)) == peg) {
				++cursor;
			} else {
				consistent.remove(cursor);
			}
		}
		if (consistent.isEmpty()) {
			return null;
		}
		return consistent.get(Project2.rangen.nextInt(consistent.size()));
	}
    /**
     *  It's true if the instance has the same colors in the same order as the Code in its argument
     * @param code
     * @return boolean
     */
	public boolean equals(Code code) {
		for (int i = 0; i < 4; ++i) {
			if (this.slots[i] != code.slots[i]) {
				return false;
			}
		}
		return true;
	}
    /**
     * toString method for printing the Code with spaces and bracket 
     */
	public String toString() {
		String rtn = "";
		for (int i = 0; i < 4; ++i) {
			rtn += this.slots[i] + " ";
		}
		return "[ " + rtn + "]";
	}
}
