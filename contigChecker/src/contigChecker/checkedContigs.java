package contigChecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class checkedContigs {

	File tempCandidateContigFolder ;
	String tempCandidateContigFile;
	File tempTargetContigsFolder;
	String tempTargetContigFile;
	File currentInputFile ;
	LinkedList<String> ContigsLinkList=new LinkedList<String> ();
	String inputFile;
	String root="";
	//String outputFile;
	int nbCurrentContigs=0;
	//int l=1;

	public checkedContigs(String inputFile, String outputFolder)
			throws IOException {
		
		
		root=outputFolder + "/temp";
	
		
		currentInputFile = new File(inputFile);
		ContigsLinkList=createContigLinkList(inputFile);
		createCandidateContigFile();
		createTargetContigsFile() ;			
		indexCaller();
		alignCaller();
		
		/*
		currentInputFile = new File(inputFile);
		ContigsLinkList=createContigLinkList(inputFile);
		
		int nbLoops=nbCurrentContigs-1;
		for (int loop=0;loop<nbLoops;loop++){
			//System.out.println("loop - " + loop+"  nbCurrentContigs - " + nbCurrentContigs);
			createCandidateContigFile();
			createTargetContigsFile() ;			
			indexCaller();
			alignCaller();
			currentInputFile = tempTargetContigsFolder;	
			ContigsLinkList.remove(0);
			nbCurrentContigs--;
		}
		*/
	}

	private void alignCaller() throws IOException {
																																		                     	        
		ProcessBuilder pb = new ProcessBuilder((contigChecker.alignerPath+"/bowtie2") ,"-f" ,"--very-sensitive-local" ,"-k" ,"7" ,"-x" ,"tempTarget" ,"-U" ,(contigChecker.outputFolder+"/temp/tempCandidateContigFile.fasta") ,"-S" ,"tempTargetAlgnmnt.sam");		
		Map<String, String> env = pb.environment();
		// If you want clean environment, call env.clear() first
		// env.clear()
		env.put("VAR1", "myValue");
		env.remove("OTHERVAR");
		env.put("VAR2", env.get("VAR1") + "suffix");

		File workingFolder = new File(root);
		pb.directory(workingFolder);

		Process proc = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null)
		{
			System.out.println(s);
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null)
		{
			System.out.println(s);
		}
		
	}
	
	private void indexCaller() throws IOException {
		
		
		ProcessBuilder pb = new ProcessBuilder((contigChecker.alignerPath+"/bowtie2-build"),(contigChecker.outputFolder+"/temp/tempTargetContigFile.fasta"),"tempTarget");		
		Map<String, String> env = pb.environment();
		// If you want clean environment, call env.clear() first
		// env.clear()
		env.put("VAR1", "myValue");
		env.remove("OTHERVAR");
		env.put("VAR2", env.get("VAR1") + "suffix");
		
		File workingFolder = new File(root );
		System.out.println("workingFolder - " + workingFolder);
		pb.directory(workingFolder);

		Process proc = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null)
		{
			System.out.println(s);
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null)
		{
			System.out.println(s);
		}

	}



	private LinkedList<String>  createContigLinkList(String inputFile) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	    LinkedList<String>linkedList = new LinkedList<>();
	    
	    // do reading, usually loop until end of contig
	    StringBuilder sb = new StringBuilder();
	    String mContig = reader.readLine();
	    String mLine=reader.readLine();

	    int c=0;
	    while (mContig != null){
	    	while ((mLine != null) && (!mLine.substring( 0, 1 ).equals(">"))) {
	    	//System.out.println("entered - " + (++nb));
	    	mContig+="\n"+mLine;    
	    	mLine=reader.readLine();
	    	} 
	    	linkedList.add(mContig);
	    	nbCurrentContigs++;
	    	mContig = mLine;
	    	mLine=reader.readLine();
	    }
	   
	    reader.close();
	    return reverseLinkedList(linkedList);
	     
	}

	private LinkedList<String>  reverseLinkedList(LinkedList<String> origList) {
		LinkedList<String> reversedList = new LinkedList<>();
		for (int i=origList.size()-1;i>=0;i--){
			reversedList.add(origList.get(i));
		}
		return reversedList;
		
		
	}

	private void createCandidateContigFile() throws FileNotFoundException {
		
		tempCandidateContigFolder = new File(root );
		tempCandidateContigFolder.mkdirs();
		tempCandidateContigFile=root +"/tempCandidateContigFile"+".fasta";
		
		try(  PrintWriter out = new PrintWriter( tempCandidateContigFile)  ){
		    out.println( ContigsLinkList.getFirst());
		}		
	}

	
	private void createTargetContigsFile() throws FileNotFoundException {
		
		//tempCandidateContigFile = new File(root + "/temp" );
		tempTargetContigFile= root +"/tempTargetContigFile"+".fasta";
		try(  PrintWriter out = new PrintWriter( tempTargetContigFile)  ){
			for (int cc=0;cc<nbCurrentContigs;cc++){
				out.println( ContigsLinkList.get(cc));
			}
		}		
	}
	
	
	
	
}


