package main;

import interfaces.List;
import data_structures.ArrayList;

/* Creates a ballot based on the line it receives. The format for line is
id#,candidate_name . It also receives a List of all the candidates in the
elections.*/
public class Ballot {
	private int ballotNum;
	private int rank;
	private int candidateID;
	private ArrayList<Integer> rankList = new ArrayList<>();
	private ArrayList<Integer> candidateIDList = new ArrayList<>();
	private List<Candidate> candidates;
	private String line;
	
	public Ballot(String line, List<Candidate> candidates) {
		this.candidates = candidates;
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
	
	// Returns the ballot number
	public int getBallotNum() {
		return this.ballotNum;
	}
	
	//Returns the rank for that candidate, if no rank is available return -1
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
	
	//Returns the candidate with that rank, if no candidate is available return -1.
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
	
	// Eliminates the candidate with the given id
	public boolean eliminate(int candidateId) {
		
		/* Me quede reemplementing this method whilst using the ArrayList method as reference */
		
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
	
	// Check if no numbers are being repeated in the candidateIDList
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
    
    
	
	/* Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 -
	invalid */
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