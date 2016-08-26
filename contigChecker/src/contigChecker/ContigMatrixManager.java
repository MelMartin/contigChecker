package contigChecker;

public class ContigMatrixManager {
	
	public ContigMatrixManager(){
		//System.out.println("checkedContigs.root:" +checkedContigs.root);
		SAMparser samP=new SAMparser(checkedContigs.root+"/tempTargetAlgnmnt.sam");
	}

}
