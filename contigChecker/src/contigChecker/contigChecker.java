package contigChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class contigChecker {
	
	
	static String inputFile ="C://Users//Mel//Documents//BIOINFORMATICS//DELFT_Research//Data//SimulatedAssembly//Newbler500//454AllContigs.fna";
	static String outputFolder ="C:/Users/Mel/Documents/BIOINFORMATICS/DELFT_Research/Codes/ContigChecker/outputContigCheckerTest.txt";
	static String alignerPath="/home/nfs/mcarbajomartin/bowtie2-2.2.7";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length > 0) {
				inputFile = args[0];	
				outputFolder = args[1];
				alignerPath = args[2];
				doTheSame();
			
		}else{
			doTheSame();
		}

	}


	private static void doTheSame() {
		try {
			checkedContigs cc=new checkedContigs(inputFile,outputFolder);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
