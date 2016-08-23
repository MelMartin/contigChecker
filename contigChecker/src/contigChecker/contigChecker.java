package contigChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class contigChecker {
	
	
	static String inputFile ="C://Users//Mel//Documents//BIOINFORMATICS//DELFT_Research//Data//SimulatedAssembly//Newbler500//454AllContigs.fna";
	static String outputFile ="C://Users//Mel//Documents//BIOINFORMATICS//DELFT_Research//Data//SimulatedAssembly//Newbler500//contigsChecked.txt";


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length > 0) {
				inputFile = args[0];	
				//outputFile = args[1];
				doTheSame();
			
		}else{
			doTheSame();
		}

	}


	private static void doTheSame() {
		// TODO Auto-generated method stub
		try {
			checkedContigs cc=new checkedContigs(inputFile,outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("UnsupportedEncodingException");
			e.printStackTrace();
		}
		
	}

}
