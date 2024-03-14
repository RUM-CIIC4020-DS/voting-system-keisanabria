package main;

/* Creates a Candidate from the line. The line will have the format
ID#,candidate_name. */

public class Candidate {
    private int ID;
    private String candidateName;
    private boolean active = true;
  
    public Candidate(String line) {
      this.ID = Integer.parseInt(line.split(",")[0]);
      this.candidateName = line.split(",")[1];
    }

    // returns the candidate’s id
    public int getId() {
        return this.ID;
    }

    // Whether the candidate is still active in the election
    public boolean isActive() {
      return this.active;
    }

    // return the candidates name
    public String getName(){
      return this.candidateName;
    }

}