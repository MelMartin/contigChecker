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
import java.util.Scanner;

public class checkedContigs {

	File tempCandidateContigFile ;//contigsChecked.txt";
	File tempTargetContigsFile;
	File currentInputFile ;
	LinkedList<String> ContigsLinkList=new LinkedList<String> ();
	String inputFile;
	String root="";
	int nbCurrentContigs=0;
	int l=1;

	public checkedContigs(String inputFile, String outputfile)
			throws IOException {
		int endIndex = inputFile.lastIndexOf("/");		
		if (endIndex != -1)  
		{
			root = inputFile.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
		}
		currentInputFile = new File(inputFile);
		ContigsLinkList=createContigLinkList(inputFile);
		
		int nbLoops=nbCurrentContigs-1;
		for (int loop=0;loop<nbLoops;loop++){
			//System.out.println("loop - " + loop+"  nbCurrentContigs - " + nbCurrentContigs);
			createCandidateContigFile();
			createTargetContigsFile() ;
			currentInputFile = tempTargetContigsFile;	
			ContigsLinkList.remove(0);
			nbCurrentContigs--;
		}
		
	}

	private LinkedList<String>  createContigLinkList(String inputFile) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	    LinkedList<String>linkedList = new LinkedList<>();
	    
	    // do reading, usually loop until end of contig
	    StringBuilder sb = new StringBuilder();
	    String mContig = reader.readLine();
	    String mLine=reader.readLine();
	    
	    //System.out.println("mLine - " +mLine);	
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
		//File currentInputFile = new File(inputFile);
		
		tempCandidateContigFile = new File(root + "/temp" );
		tempCandidateContigFile.mkdirs();
		
		try(  PrintWriter out = new PrintWriter( tempCandidateContigFile+"/tempCandidateContigFile"+l+".fasta")  ){
		    out.println( ContigsLinkList.getFirst());
		}		
	}

	
	private void createTargetContigsFile() throws FileNotFoundException {
		
		//tempCandidateContigFile = new File(root + "/temp" );
		try(  PrintWriter out = new PrintWriter( root + "/temp"+"/tempTargetContigFile"+l+".fasta")  ){
			for (int cc=1;cc<nbCurrentContigs;cc++){
				out.println( ContigsLinkList.get(cc));
			}
			l++;
		    //
		}		
	}
	
	
	
	
}


