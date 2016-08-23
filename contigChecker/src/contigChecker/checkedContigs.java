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

public class checkedContigs {

	File tempCandidateContigFile ;//contigsChecked.txt";

	public checkedContigs(String inputFile, String outputfile)
			throws FileNotFoundException, UnsupportedEncodingException {
		getLastContig(inputFile);
	}

	private void getLastContig(String inputFile) {
		File file = new File(inputFile);
		int endIndex = inputFile.lastIndexOf("/");
		String newstr="";
		if (endIndex != -1)  
		{
			newstr = inputFile.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
		}
		tempCandidateContigFile = new File(newstr + "/temp" );
		tempCandidateContigFile.mkdirs();
		//System.out.println("tempCandidateContigFile - " +tempCandidateContigFile);
		readFromLast(file);

	}

	// Read last line of the file
	public void readFromLast(File file){


		StringBuilder builder = new StringBuilder();
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
			long fileLength = file.length() - 1;
			// Set the pointer at the last of the file
			randomAccessFile.seek(fileLength);
			long pointer;
			for( pointer = fileLength; pointer >= 0; pointer--){
				randomAccessFile.seek(pointer);
				char c;
				// read from the last one char at the time
				c = (char)randomAccessFile.read(); 
				// break when end of the line
				if(c == '>'){
					System.out.println("pointer: "+pointer);
					builder.append(c);
					break;
				}
				builder.append(c);
			}
			// Since line is read from the last and is in reverse, use reverse method to make it right
			builder.reverse();

			PrintWriter out = new PrintWriter( tempCandidateContigFile+"/tempCandidateContigFile.txt" );
			out.println( builder.toString() );
			
			//Now, the rest of the contigs (until the last one)
			builder = new StringBuilder();
			for( long pointertwo = 0; pointertwo <= pointer; pointertwo++){
				randomAccessFile.seek(pointertwo);
				char c;
				c = (char)randomAccessFile.read(); 
				// break when end of the line

				builder.append(c);
			}    
			PrintWriter  out2 = new PrintWriter( tempCandidateContigFile+"/tempTargetContigFile.txt" ) ;
			out.println( builder.toString() );
			
			//System.out.println("Line - " + builder.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(randomAccessFile != null){
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}


