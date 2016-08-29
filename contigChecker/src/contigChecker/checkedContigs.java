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
	File alignmentFile;
	LinkedList<String> ContigsLinkList=new LinkedList<String> ();
	String inputFile;
	static String root="";
	//String outputFile;
	int nbCurrentContigs=0;
	SAMparser samP;
	FileWriter fw ;
	static boolean isFirstWrite=true;
	
	//int l=1;

	public checkedContigs(String inputFile, String outputFolder)
			throws IOException {
		int lastIndexOf=outputFolder.lastIndexOf("/");
		root=outputFolder.substring(0,lastIndexOf) + "/tempContigs/temp";
		
		/*
		TEST WITHOUT LOOP (ONE SINGLE CONTIG COMBINATION)
		currentInputFile = new File(inputFile);
		ContigsLinkList=createContigLinkList(inputFile);
		alignmentFile = new File(root+"/targetAlgnmnts.sam" );
		//alignmentFile.mkdirs();
		
		createCandidateContigFile();
		createTargetContigsFile() ;			
		indexCaller();
		alignCaller();
		printToAlignmentFile();
		*/
		
		
		/*CODE WITH LOOP OVER ALL CONTIG COMBINATIONS	* */
		
		currentInputFile = new File(inputFile);
		ContigsLinkList=createContigLinkList(inputFile);
		
		alignmentFile = new File(root+"/targetAlgnmnts.sam" );
		//alignmentFile.mkdirs();
		
		int nbLoops=nbCurrentContigs-1;
		for (int loop=0;loop<nbLoops;loop++){
			//System.out.println("loop - " + loop+"  nbCurrentContigs - " + nbCurrentContigs);
			createCandidateContigFile();
			createTargetContigsFile() ;			
			indexCaller();
			alignCaller();
			printToAlignmentFile();
			currentInputFile = tempTargetContigsFolder;	
			ContigsLinkList.remove(0);
			nbCurrentContigs--;	
		}
		
	}

	private void printToAlignmentFile() {
		
		samP=new SAMparser(root+"/targetAlgnmnts.sam");
		
		samP.printContigList();
		File repeatedContigsFile = new File(root+"/repeatedContigs.txt");
		try{
			repeatedContigsFile.createNewFile(); // if file already exists will do nothing 
		}catch( IOException f){
			
		}
		
		try(FileWriter fw = new FileWriter(repeatedContigsFile, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
				String outst=samP.getHeader();
				if (outst!=null){
					out.println(samP.getHeader());
				}
			    if (samP.contigsList.size()>0){
			    	out.println(samP.printContigList());
			    }
				
			    
			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
		
	}
	
	private void alignCaller() throws IOException {
																																		                     	        
		ProcessBuilder pb = new ProcessBuilder((contigChecker.alignerPath+"/bowtie2") ,"-f" ,"--very-sensitive-local" ,"-k" ,"7" ,"-x" ,"tempTarget" ,"-U" ,(contigChecker.outputFolder+"/temp/tempCandidateContigFile.fasta") ,"-S" ,"targetAlgnmnts.sam");		
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
		String s = null;
		/*System.out.println("Here is the standard output of the command:\n");
		
		while ((s = stdInput.readLine()) != null)
		{
			System.out.println(s);
		}
		 */
		// read any errors from the attempted command
		s = stdError.readLine();
		if (s != null){
			System.out.println("Here is the standard error of the command (if any):\n");
			while (s != null)
			{
				System.out.println(s);
				s = stdError.readLine();
			}
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
		//System.out.println("workingFolder - " + workingFolder);
		pb.directory(workingFolder);

		Process proc = pb.start();

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		// read the output from the command
		String s = null;
		/*
		System.out.println("Here is the standard output of the command:\n");
		
		while ((s = stdInput.readLine()) != null)
		{
			System.out.println(s);
		}
		 */
		// read any errors from the attempted command
		s = stdError.readLine();
		if (s != null){
			System.out.println("Here is the standard error of the command (if any):\n");
			while (s != null)
			{
				System.out.println(s);
				s = stdError.readLine();
			}
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
	    	while ((mLine != null) && (!mLine.substring( 0, 1 ).equals(">"))) {//reading a sequence
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
		//System.out.println("****Root"+root);
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


