package main;

import interfaces.List;
import data_structures.ArrayList;

/**
 * A class that is used to process and analyze ballots in an election.
 * <p>
 * It processes and analyzes 
 * ballots in an election, utilizing ArrayLists to store ranks 
 * and candidate IDs and a List for storing Candidate objects, 
 * allowing for efficient data manipulation and integration 
 * within the election system.
 */
public class Ballot {
	private int ballotNum;
	private int rank;
	private int candidateID;
	private ArrayList<Integer> rankList = new ArrayList<>();
	private ArrayList<Integer> candidateIDList = new ArrayList<>();
	private List<Candidate> candidates;
	private String line;
	
	/**
	 * Creates a ballot based on the line it receives. 
	 * The format for line is id#,candidate_name . It 
	 * also receives a List of all the candidates in the
	elections.
	 * <p>
	 *  The algorithm utilizes ArrayLists to manage `rankList`
	 *   and `candidateIDList`, storing ranks and candidate 
	 *   IDs respectively for each candidate in the ballot. They are used
	 *    because they provide dynamic resizing, efficient 
	 *    insertion and retrieval operations, essential for 
	 *    storing ranks and candidate IDs, allowing flexibility 
	 *    in managing data structures during runtime.
	 *  <p>
	 *  @param line containing ballots' information
	 *  @param candidates a list of the candidate's information
	*/
	public Ballot(String line, List<Candidate> candidates) {
		this.candidates = candidates;
		int size = candidateIDList.size();
		this.line = line;
		String[] sections = null;
		
		if(getBallotType() != 1) {
			sections = line.split(",");
		}
		
		if(sections != null) {
			
			for(int i = 1; i < sections.length; i++) {
		        rank = Integer.parseInt(sections[i].split(":")[1]);
		        rankList.add(rank);
		        
		        candidateID = Integer.parseInt(sections[i].split(":")[0]);
		        candidateIDList.add(candidateID);
		    }
			
			this.ballotNum = Integer.parseInt(sections[0]);
			
		} else {
			this.ballotNum = Integer.parseInt(line);
		}
		
//		System.out.println("rankList: " + rankList);
//        System.out.println("candidateIDList: " + candidateIDList);
		
	}
	
	/**
     * Gets the ballot number
     * <p>
     * It returns the ballot number found in the constructor.
     * <p>
	 *  @return the ballot number
     */
	public int getBallotNum() {
		return this.ballotNum;
	}
	
	/**
	 * Gets the rank of the input candidate 
	 * <p>
	 * The algorithm uses ArrayLists for both candidateIDList 
	 * and rankList. ArrayLists are used because they provide 
	 * dynamic sizing, efficient random access, and allow for 
	 * easy manipulation of elements, which are essential for 
	 * storing candidate IDs and their corresponding ranks.
	 * <p>
	 * @param candidateID - ID number of the candidate
	 * @return the rank for that candidate, if no rank is available return -1
	 */
	public int getRankByCandidate(int candidateID) {
		//check that the ballot isn't empty
		// check if Candidateidlist has that id, if it does, return index
		// with the ranklist, get(index)
		//if that id doesn't exist, then return -1
		
		if(getBallotType() != 1) {
			if (candidateIDList.contains(candidateID)) {
		        for (int i = 0; i < candidateIDList.size(); i++) {
		            if (candidateIDList.get(i) == candidateID) {
		                int index = i; // Return the index if candidateID is found
				        return rankList.get(index);
		            }
		        }
		    }
		}
		
		return -1;
	}
	
	/**
	 * Gets the candidate of the input rank 
	 * <p>
	 * The algorithm uses ArrayLists for both candidateIDList 
	 * and rankList. ArrayLists are used because they provide 
	 * dynamic sizing, efficient random access, and allow for 
	 * easy manipulation of elements, which are essential for 
	 * storing candidate IDs and their corresponding ranks.
	 * <p>
	 * @param rank of the candidate to be determined
	 * @return the candidate with that rank, if no candidate is available return -1
	 */
	public int getCandidateByRank(int rank) {
		//if that rank doesn't exist, then return -1
		
		if(getBallotType() != 1) {
			if (rankList.contains(rank)) {
		        for (int i = 0; i < rankList.size(); i++) {
		            if (rankList.get(i) == rank) {
		                int index = i; // Return the index if rank is found
				        return candidateIDList.get(index);
		            }
		        }
		    }
		}
		
		return -1;
	}
	
	/**
	 * Eliminates the candidate with the given id
	 * <p>
	 * The algorithm primarily uses ArrayLists 
	 * (`candidateIDList` and `rankList`). 
	 * ArrayLists are used because they allow dynamic 
	 * resizing, which is necessary for adding and 
	 * removing elements efficiently in the context 
	 * of candidate elimination operations.
	 * <p>
	 * @param candidateId to be eliminated
	 * @return if the candidateId was successfully eliminated (true or false)
	 */
	public boolean eliminate(int candidateId) {
		
		/*
			int size = candidateIDList.size();
			
			// Check bounds
			if(candidateId < 0 || candidateId >= candidateIDList.size())
				throw new IndexOutOfBoundsException();
			// Shift values to the left
			for(int i = candidateId; i < this.candidateIDList.size()-1; i++)
				this.elements[i] = this.elements[i+1];
			// Null the last position
			this.elements[this.candidateIDList.size()-1]= null;
			// Decrease size
			size--;
			return true;
		*/
		
		// Check if getBallotType() != 1
		// Check that the candidateId exists
		// Use remove(candidateId) to eliminate the candidate
		// Just eliminate a rank #
		
		if(getBallotType() != 1) {
		    int index = -1;
		    for(int i = 0; i < candidateIDList.size(); i++) {
		        if(candidateIDList.get(i).equals(candidateId)) {
		            index = i;
		            break;
		        }
		    }
		    
		    if (index != -1) {
		        candidateIDList.remove(index);
		        rankList.remove(rankList.size()-1);
		        return true;
		    }
		}
		
		return false;

	}
	
	/**
	 * Checks if the list containing all the ranks is valid
	 * <p>
	 * The algorithm uses an ArrayList named `seenRanks` to
	 *  keep track of seen ranks and ensure there are no repeated 
	 *  numbers. It also iterates over a list called `rankList` to 
	 *  check if the ranks are in ascending order and not skipped, 
	 *  returning false if any condition fails. Using an ArrayList 
	 *  for `seenRanks` is efficient because it provides constant-time 
	 *  (O(1)) lookup for checking if a rank has already been seen, 
	 *  ensuring quick verification of repeated numbers during
	 *   iteration.
	 * <p>
	 * @param candidateId to be eliminated
	 * @return if the candidateId was successfully eliminated (true or false)
	 */
	public boolean isValidRankList() {
        int prevRank = -1;
        ArrayList<Integer> seenRanks = new ArrayList<>();

        for (int r : rankList) {
            // Check if the rank is in ascending order and not skipped
            if (r <= prevRank) {
//            	System.out.println(r);
                return false; // Not in ascending order
            } else if (prevRank != -1 && r != prevRank+1) {
                return false; // No skipped number
            }
            
            prevRank = r;

            // Check for repeated numbers
            if (seenRanks.contains(r)) {
                return false; // Repeated number found
            }
            seenRanks.add(r);
        }
        
        return true; // All conditions satisfied
    }
	
	/**
	 * Check if no numbers are being repeated in the candidateIDList
	 * <p>
	 * The algorithm uses an ArrayList to track seen candidate IDs 
	 * and efficiently checks for duplicates using the contains 
	 * method, ensuring that each candidate ID is unique within 
	 * the list. This approach is efficient because ArrayList 
	 * provides constant time lookup with contains, and tracking 
	 * seen candidates helps avoid iterating over the entire list 
	 * repeatedly.
	 * <p>
	 * @return if the candidateIDList contains no duplicates (true or false)
	 */
    public boolean candidateIDListHasNoDuplicates() {
        ArrayList<Integer> seenCandidates = new ArrayList<>();

        for (int candidateID : candidateIDList) {
            if (seenCandidates.contains(candidateID)) {
                return false; // Found a duplicate
            }
            seenCandidates.add(candidateID);
        }
        return true; // No duplicates found
    }
    
    
    /**
	 * Gets the ballot type
	 * <p>
	 * The algorithm uses ArrayLists for `rankList` and 
	 * `candidateIDList`. ArrayLists are used because they 
	 * provide dynamic resizing and efficient random access, 
	 * which is suitable for storing and accessing ballot 
	 * data efficiently.
	 * <p>
	 * @return an integer that indicates if the 
	 * ballot is: 0 – valid, 1 – blank or 2 - invalid
	 */
	public int getBallotType() {
		if(isValidRankList() == false || !candidateIDListHasNoDuplicates()){
			return 2;
		} else if(!line.contains(",")) {
			return 1;
		}
		
		return 0;

		// [x] check that candidateIDList is the same length as the candidates
		// [x] check that the rankList is in ascending order and no numbers are skipped (each iteration is one digit ahead) & that there aren't repeated numbers
		// [x] check that no numbers are being repeated in the candidateIDList
		// [x] check if there is only the ballotNum (blank ballot) -> output: 1
	}
}