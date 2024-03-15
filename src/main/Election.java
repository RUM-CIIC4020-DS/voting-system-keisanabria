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
	private ArrayList<Integer> lowestVotedCandidates = new ArrayList<Integer>();
	
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
		
		// Read through candidates.csv to convert each line into Candidate -> put each in a list
		try {
		    String candidateLine;
		    while ((candidateLine = this.readerCandidates.readLine()) != null) {
		        Candidate candidate = new Candidate(candidateLine);
		        candidateList.add(candidate);
		    }
		    this.readerCandidates.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}

		// Initialize a 2D ArrayList to store the count of votes for each candidate and rank
		ArrayList<ArrayList<Integer>> voteCountList = new ArrayList<>(candidateList.size());
		for (int i = 0; i < candidateList.size(); i++) {
		    ArrayList<Integer> candidateVotes = new ArrayList<>(candidateList.size());
		    // Initialize each candidate's vote count for each rank to 0
		    for (int j = 0; j < candidateList.size(); j++) {
		        candidateVotes.add(0);
		    }
		    voteCountList.add(candidateVotes);
		}

		try {
		    String ballotLine;
		    while ((ballotLine = this.readerBallots.readLine()) != null) {
		        Ballot ballot = new Ballot(ballotLine, candidateList);
		        if (ballot.getBallotType() != 1 && ballot.getBallotType() != 2) {
		            // Loop through each rank in the ballot to count the votes for each candidate
		            for (int i = 1; i <= candidateList.size(); i++) {
		                int candidateID = ballot.getCandidateByRank(i);
		                if (candidateID != -1) {
		                    int candidateIndex = candidateID - 1; // Adjust index to match the list index
		                    int currentCount = voteCountList.get(candidateIndex).get(i - 1);
		                    voteCountList.get(candidateIndex).set(i - 1, currentCount + 1); // Increment vote count for the corresponding rank
		                }
		            }
		        }
		    }
		    this.readerBallots.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}

		
		// -------------------------------------------------------------------------------------------------------
		
		double fiftypercent = 0.5*getTotalValidBallots();
		int rankCount = 0;
		int rankNum = 0;
		int lowestVotes = voteCountList.get(0).get(0);
		int LVCandidateID;
		boolean check = false;
		boolean multiplesMethodTrigger = false;
		
		while(rankCount < candidateList.size()) {
			while(rankNum < candidateList.size()) {
				
				if(!multiplesMethodTrigger) {
					// Return winner if they have > 50% votes
					if(voteCountList.get(rankNum).get(rankCount) > fiftypercent) {
						return candidateList.get(rankNum).getName();
					}
					
					// If there's no winner, iterate for the lowest number of votes
					if(voteCountList.get(rankNum).get(rankCount) < lowestVotes) {
						lowestVotes = voteCountList.get(rankNum).get(rankCount);
					}
				} else {
					for(int candidateIndex : lowestVotedCandidates) {
						if(voteCountList.get(rankNum).get(candidateIndex) < lowestVotes) {
							lowestVotes = voteCountList.get(rankNum).get(candidateIndex);
						}
					}
				}
				
				LVCandidateID = candidateList.get(rankNum).getId(); // Lowest Votes' Candidate ID
				rankNum++;
			}
			
			if(rankVotesHasNoDuplicates(rankCount, voteCountList)) {
				// active status -> false
				check = true;
				break;
			}
			
			multiplesMethodTrigger = true;
			rankNum = lowestVotedCandidates.get(0);
			rankCount++;
			if(!multiplesMethodTrigger) {
				lowestVotes = voteCountList.get(0).get(rankCount);
			} else {
				lowestVotes = voteCountList.get(0).get(lowestVotedCandidates.get(0));
			}
		}
		
		if(check) {
			//eliminate LVCandidateID
			// turn check back to false!
			// Create an update method to implement here
		}
		
		// If no one was eliminated, eliminate the candidate with the longest ID#
		if(rankCount == candidateList.size()-1) {
			// eliminate longest ID#
			// Implement update method here
		}
		
		// (TODO: At the end) Every time you eliminate a candidate, add it to a global variable to count and return in getEliminatedCandidates()
		
	}
	
	public void updateVoteCountList() {
		// Get eliminated candidate's ID#
		// With this, lower numbered rankNum increase
		/* Me quede implementing this method */
		
	}
	
	public boolean rankVotesHasNoDuplicates(int n, ArrayList<ArrayList<Integer>> nm) {
		ArrayList<Integer> rankList = new ArrayList<Integer>();
		
		for(ArrayList<Integer> m : nm) {
			rankList.add(m.get(n));
		}
		
        ArrayList<Integer> seenVotes = new ArrayList<>();
        
        for(int votes = 0; votes < rankList.size(); votes++) {
        	if(seenVotes.contains(rankList.get(votes))) {
        		lowestVotedCandidates.add(votes); // Storing ID#s / candidates' indexes
        		return false; // Found a duplicate
        	}
        	seenVotes.add(rankList.get(votes));
        }
        return true; // No duplicates found
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