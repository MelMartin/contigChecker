package contigChecker;

public class ContigData {

	String contigName;//the name of this contig (target)
	String candidateContig;//the name of the candidate contig to which the alignment measures of this contig refer to
	int length;
	int as;//Alignment Score
	int nm;//Editing distance	
	

	public ContigData(String targetName, String candidate, int as_, int nm_) {
		contigName = targetName;
		candidateContig=candidate;
		as=as_;
		nm=nm_;
	}
	
	public String getContigName() {
		return contigName;
	}

	public String getCandidateName() {
		return candidateContig;
	}	
		
	public int getAs() {
		return as;
	}

	public void setAs(int as) {
		this.as = as;
	}

	public int getNm() {
		return nm;
	}

	public void setNm(int nm) {
		this.nm = nm;
	}	
	
}
