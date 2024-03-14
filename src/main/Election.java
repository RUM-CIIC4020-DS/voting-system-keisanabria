package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import main.Candidate;
import main.Ballot;

public class Election {
	private BufferedReader readerCandidates;
	private BufferedReader readerBallots;
	private ArrayList<Candidate> candidateList = new ArrayList<>();
	
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
	public String getWinner() {
		/*
		1. Read through the candidates.csv and store each line in a list -> this will be
		the candidates list that has to be used to create a ballot object 
		2. Initialize an array list of size n ; n = amount of candidates
		3. Read the file -> store ballot string using ballot class -> check that the type is not empty nor invalid -> 
		in that ballot, getCandidateByRank(1) -> adds the ballot to the array list of size n
		THIS IS DONE FOR THE PURPOSE OF WHEN ELIMINATING A CANDIDATE!
		*/
		
		// Read through candidates.csv to convert each line into Candidate -> put each in a list
		ArrayList<ArrayList<String>> ballotList = new ArrayList<>();
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
		
		// Every time you eliminate a candidate, add it to a global variable to count and return in getEliminatedCandidates()
		
	}
	
	// returns the total amount of ballots submitted
	public int getTotalBallots() {
		
		int ballotCount = 0;
		
		try {
			String ballotLine;
			while ((ballotLine = this.readerBallots.readLine()) != null) {
				ballotCount++;
			}
			this.readerBallots.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ballotCount;

	}
	
	// returns the total amount of invalid ballots
	public int getTotalInvalidBallots() {
		int invalidBallotsCount = 0;
		
		try {
			String ballotLine;
			while ((ballotLine = this.readerBallots.readLine()) != null) {
				Ballot ballot = new Ballot(this.readerBallots.readLine(), candidateList);
				if(ballot.getBallotType() == 2) {
					invalidBallotsCount++;
				}
			}
			this.readerBallots.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return invalidBallotsCount;
	}
	
	// returns the total amount of blank ballots
	public int getTotalBlankBallots() {
		int blankBallotsCount = 0;
		
		try {
			String ballotLine;
			while ((ballotLine = this.readerBallots.readLine()) != null) {
				Ballot ballot = new Ballot(this.readerBallots.readLine(), candidateList);
				if(ballot.getBallotType() == 1) {
					blankBallotsCount++;
				}
			}
			this.readerBallots.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return blankBallotsCount;
	}
	
	// returns the total amount of valid ballots
	public int getTotalValidBallots() {
		int validBallotsCount = 0;
		
		try {
			String ballotLine;
			while ((ballotLine = this.readerBallots.readLine()) != null) {
				Ballot ballot = new Ballot(this.readerBallots.readLine(), candidateList);
				if(ballot.getBallotType() == 0) {
					validBallotsCount++;
				}
			}
			this.readerBallots.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return validBallotsCount;
	}
	
	/* List of names for the eliminated candidates with the numbers of 1s they had,
	must be in order of elimination. Format should be <candidate name>-<number of 1s
	when eliminated>*/
	public List<String> getEliminatedCandidates();
	
	/**
	* Prints all the general information about the election as well as a
	* table with the vote distribution.
	* Meant for helping in the debugging process.
	*/
	/*
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
	*/
	
}