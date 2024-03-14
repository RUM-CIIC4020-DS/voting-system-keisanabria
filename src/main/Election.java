package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import main.Candidate;

public class Election {
	private BufferedReader readerCandidates;
	private BufferedReader readerBallots;
	
	/* Constructor that implements the election logic using the files candidates.csv
	and ballots.csv as input. (Default constructor) */
	public Election() {
		BufferedReader readerCandidates = new BufferedReader(new FileReader("inputFiles/candidates.csv"));
		BufferedReader readerBallots = new BufferedReader(new FileReader("inputFiles/ballots.csv"));
		
		this.readerCandidates = readerCandidates;
		this.readerBallots = readerBallots;
	}
	
	/* Constructor that receives the name of the candidate and ballot files and applies
	the election logic. Note: The files should be found in the input folder. */
	public Election(String candidates_filename, String ballots_filename) {
		
		BufferedReader readerCandidates = new BufferedReader(new FileReader("inputFiles/" + candidates_filename));
		BufferedReader readerBallots = new BufferedReader(new FileReader("inputFiles/" + ballots_filename));
		
		this.readerCandidates = readerCandidates;
		this.readerBallots = readerBallots;

	}
	
	// returns the name of the winner of the election
	public String getWinner();
	
	// returns the total amount of ballots submitted
	public int getTotalBallots() {
		
		/*
			1. Read through the candidates.csv and store each line in a list -> this will be
			the candidates list that has to be used to create a ballot object 
			2. Read the file -> store ballot string using ballot class 
			WITHOUT INCLUDING EMPTY AND INVALID BALLOTS:
			3. Store ballots in ballot ArrayList
			4. Store ballot ArrayList into ballotList -> according to the first ballot, 
			store the lists such as the ranks go down as the list moves to the right
		*/
		
		// Read through candidates.csv to convert each line into Candidate -> put each in a list
		ArrayList<Candidate> candidateList = new ArrayList<>();
		try {
			String candidateLine;
			while ((candidateLine = this.readerCandidates.readLine()) != null) {
				Candidate candidate = new Candidate(this.readerCandidates.readLine());
				candidateList.add(candidate);
			}
			this.readerCandidates.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// --
		
		ArrayList<String> ballot = new ArrayList<String>();
		ArrayList<ArrayList<String>> ballotList = new ArrayList<>();
		
		try {
			String lines;
			while ((lines = this.readerBallots.readLine()) != null) {
				ballot.add(this.readerBallots.readLine()); // Returns a string -> better to save it into a variable or print it out!
				ballotList.add(ballot);
			}
			this.readerBallots.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	// returns the total amount of invalid ballots
	public int getTotalInvalidBallots();
	
	// returns the total amount of blank ballots
	public int getTotalBlankBallots();
	// returns the total amount of valid ballots
	public int getTotalValidBallots();
	/* List of names for the eliminated candidates with the numbers of 1s they had,
	must be in order of elimination. Format should be <candidate name>-<number of 1s
	when eliminated>*/
	public List<String> getEliminatedCandidates();
	
	/**
	* Prints all the general information about the election as well as a
	* table with the vote distribution.
	* Meant for helping in the debugging process.
	*/
	public void printBallotDistribution() {
	 System.out.println("Total blank ballots:" + getTotalBlankBallots());
	 System.out.println("Total invalid ballots:" + getTotalInvalidBallots());
	 System.out.println("Total valid ballots:" + getTotalValidBallots());
	 System.out.println(getEliminatedCandidates());
	 for(Candidate c: this.getCandidates()) {
	 System.out.print(c.getName().substring(0, c.getName().indexOf(" ")) + "\t");
	 for(Ballot b: this.getBallots()) {
	 int rank = b.getRankByCandidate(c.getId());
	 String tableline = "| " + ((rank != -1) ? rank: " ") + " ";
	 System.out.print(tableline);
	 }
	 System.out.println("|");
	 }System.out.println("Total ballots:" + getTotalBallots());
	 
	}
}