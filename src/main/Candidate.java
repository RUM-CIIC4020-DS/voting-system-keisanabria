package main;

/**
 * A class that implements the Candidate logic of the program.
 * <p>
 * It gets the ID#, their status, and their name using the split method
 * to create a list of the instance rapidly.
 */
public class Candidate {
    private int ID;
    private String candidateName;
    private boolean active = true;
  
    /**
     * Creates a Candidate from the line. The line will have the format
     * ID#,candidate_name.
     * <p>
     * It uses arrays of strings due to its 
     * reliance on the split() method, which breaks a string into 
     * an array of substrings based on a delimiter. Specifically, 
     * it splits the input line by commas (,).
     * <p>
     * @param line represents the string that contains the information
     * of the Candidate
     */
    public Candidate(String line) {
      this.ID = Integer.parseInt(line.split(",")[0]);
      this.candidateName = line.split(",")[1];
    }
    
    /**
     * Gets the candidate's ID number
     * <p>
     * It returns the ID# found in the constructor.
     * <p>
     * @return the candidate’s id
     */
    public int getId() {
        return this.ID;
    }
    
    /**
     * Defines whether the candidate 
     * is still active in the election
     * <p>
     * @return if it is true that the candidate
     * is active.
     */
    public boolean isActive() {
      return this.active;
    }
    
    /**
     * Gets the candidate's name
     * <p>
     * It returns the name found in the constructor.
     * <p>
     * @return the candidates name
     */
    public String getName(){
      return this.candidateName;
    }

}