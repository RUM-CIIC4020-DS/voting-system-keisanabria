package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import data_structures.ArrayList;
import interfaces.List;
import main.Candidate;
import main.Ballot;

/**
 * A class that implements the Election logic of the program.
 * <p>
 * It reads the inputFiles to save the candidatesList and ballotsList.
 * <p>
 * It determines the winner whilst using ArrayLists for 
 * the purpose of storing the Candidates' and Ballots' information, 
 * the votes per rank count, the count of the lowest voted 
 * candidates, among others. This was done to facilitate the iteration
 * through those values.
 */
@SuppressWarnings("unused")
public class Election {
	private BufferedReader readerCandidates;
	private BufferedReader readerBallots;
	private ArrayList<Candidate> candidateList = new ArrayList<>();
	private ArrayList<Integer> lowestVotedCandidates = new ArrayList<Integer>();
	private int eliminatedCandidatesCount = 0;
	List<String> eliminatedCandidates = new ArrayList<>();
	
	/**
	 * Constructor that implements the election logic using 
	 * the files candidates.csv and ballots.csv as input. 
	 * (Default constructor) 
	 * 
	 *  BufferedReader objects were used to open the 
	 *  candidate.csv and ballots.csv files and they
	 *  were saved in the class to facilitate the
	 *  use of them.
	*/
	public Election() {
		BufferedReader readerCandidates = null;
		BufferedReader readerBallots = null;
		try {
			readerCandidates = new BufferedReader(new FileReader("inputFiles/candidates.csv"));
			readerBallots = new BufferedReader(new FileReader("inputFiles/ballots.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		this.readerCandidates = readerCandidates;
		this.readerBallots = readerBallots;
	}
	
	/**
	 * Constructor that receives the name of the candidate 
	 * and ballot files and applies the election logic. 
	 * Note: The files should be found in the input folder
	 * <p>
	 *  BufferedReader objects were used to open the 
	 *  candidate.csv and ballots.csv files and they
	 *  were saved in the class to facilitate the
	 *  use of them.
	 *  <p>
	 *  @param candidates_filename file containing candidates' information
	 *  @param ballots_filename file containing ballots' information
	*/
	public Election(String candidates_filename, String ballots_filename) {
		
		BufferedReader readerCandidates = null;
		BufferedReader readerBallots = null;
		try {
			readerCandidates = new BufferedReader(new FileReader("inputFiles/" + candidates_filename));
			readerBallots = new BufferedReader(new FileReader("inputFiles/" + ballots_filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		this.readerCandidates = readerCandidates;
		this.readerBallots = readerBallots;

	}
	
	/**
	 * Method that determines of the Election.
	 * <p>
	 *  The algorithm uses ArrayLists to store candidate 
	 *  information and the count of votes for 
	 *  each candidate and rank. The reason ArrayLists are 
	 *  used is because they provide dynamic resizing, 
	 *  allowing for flexibility in adding elements as 
	 *  the program runs and as more candidates and votes 
	 *  are processed. Additionally, ArrayLists allow for 
	 *  easy access and manipulation of elements, making them 
	 *  suitable for storing candidate data and vote 
	 *  counts in this scenario.
	 *  <p>
	 *  @return the name of the winner of the election
	*/
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
		int LVCandidateID = 0;
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
			// Remove the candidate from the candidateList
		    candidateList.remove(LVCandidateID);
		    
		    // Remove the corresponding row from the voteCountList
		    voteCountList.remove(LVCandidateID);
		    
		    // Remove the candidate's votes from other candidates' counts
		    for (ArrayList<Integer> votes : voteCountList) {
		        votes.remove(LVCandidateID);
		    }

		    check = false;
		}
		
		// If no one was eliminated, eliminate the candidate with the longest ID#
		if(rankCount == candidateList.size()-1) {
			candidateList.remove(candidateList.size()-1); // eliminate longest ID#
		    voteCountList.remove(candidateList.size()-1);
		    for (ArrayList<Integer> votes : voteCountList) {
		        votes.remove(LVCandidateID);
		    }
		    eliminatedCandidatesCount++;
		}
		
	    for (int i = 0; i < eliminatedCandidates.size(); i++) {
	        eliminatedCandidates.add(candidateList.get(i).getName() + "-" + voteCountList.get(i).get(0));
	    }
		
		return ""; // Dummy return
	}
	
	/**
	 * Method that checks if the count of lowest amount of votes is
	 * repeated within the candidates.
	 * <p>
	 *  The algorithm uses ArrayLists to store lists of integers,
	 *  which represent the votes and the list of ranks. These were 
	 *  chosen for their flexibility, ease of use, 
	 *  and efficient sequential access, making them suitable for 
	 *  storing and manipulating lists of integers in this algorithm.
	 *  <p>
	 *  @param n represents the rank of which duplicates of votes will be checked
	 *  @param nm is the 2D list of votes per rank
	 *  @return if there are no duplicates (true or false)
	*/
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
	
	/**
	 * Method that counts the amount of ballots
	 * in the ballot.csv file
	 * <p>
	 *  The given algorithm uses a BufferedReader 
	 *  to read lines from the file. It counts the number of lines
	 *   in the file, representing the total number of ballots.
	 *   It reads each line sequentially from the file until 
	 *   there are no more lines to read. 
	 *  <p>
	 *  @return the total amount of ballots submitted
	*/
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
	
	/**
	 * Method that counts the total amount of invalid ballots
	 * in the ballot.csv file. It checks if the getBallotType() is
	 * equal to two (which signifies that it is an Invalid Ballot).
	 * <p>
	 *  The algorithm uses a FileReader to read lines from a file, 
	 *  and it uses an ArrayList called candidateList, 
	 *  which contains instances of the Candidate class.
	 *   It also creates instances of the Ballot class, passing a string 
	 *   and the candidateList to its constructor. These were used for an
	 *   organized structure of the method.
	 *  <p>
	 *  @return the total amount of invalid ballots
	*/
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
	
	/**
	 * Method that counts the total amount of blank ballots
	 * in the ballot.csv file. It checks if the getBallotType() is
	 * equal to one (which signifies that it is an blank ballot).
	 * <p>
	 *  The algorithm uses a FileReader to read lines from a file, 
	 *  and it uses an ArrayList called candidateList, 
	 *  which contains instances of the Candidate class.
	 *   It also creates instances of the Ballot class, passing a string 
	 *   and the candidateList to its constructor. These were used for an
	 *   organized structure of the method.
	 *  <p>
	 *  @return the total amount of blank ballots
	*/
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
	
	/**
	 * Method that counts the total amount of valid ballots
	 * in the ballot.csv file. It checks if the getBallotType() is
	 * equal to zero (which signifies that it is an valid ballot).
	 * <p>
	 *  The algorithm uses a FileReader to read lines from a file, 
	 *  and it uses an ArrayList called candidateList, 
	 *  which contains instances of the Candidate class.
	 *   It also creates instances of the Ballot class, passing a string 
	 *   and the candidateList to its constructor. These were used for an
	 *   organized structure of the method.
	 *  <p>
	 *  @return the total amount of valid ballots
	*/
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
		
//		System.out.println(validBallotsCount);
		
		return validBallotsCount;
	}
	
	/**
	 * Method that shows the list of names for the 
	 * eliminated candidates with the numbers of 1s they had,
	 * must be in order of elimination. Format is 
	 * <candidate name>-<number of 1s when eliminated>
	 *  <p>
	 *  @return list of eliminated candidates
	*/
	public List<String> getEliminatedCandidates(){
		return this.eliminatedCandidates;
	}
	
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