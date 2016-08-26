package contigChecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


//WARNING this is a different class that the one with the same name in PLOEST project!!!
public class SAMparser {

	Map<String, ContigData> contigsList;// Map of ContigDatas(value) and their
										// name (key)
	int aScoreRef;
	final int ASthresholdPERCENT = 80;//percentage of Algmt Score to set the threshold
	String header;
	
	public SAMparser(String inputFile){

		// fill the contigsList
		contigsList = new HashMap<String, ContigData>();
		BufferedReader br;
		try {
			
			br = new BufferedReader(new FileReader(createNewFile("/tudelft.net/staff-bulk/ewi/insy/DBL/mcarbajo/SimulatedData/tempContigs/temp","targetAlgnmnts.sam")));
			String line = br.readLine();
			
			//Skip header
			while ( (line != null) &&(line.substring(0,1).equals("@")))  {				
				line = br.readLine();

			}
			aScoreRef=0;//alignment score threshold (reference is contig against itself), 
							//if any alignment score is below this, it won't be considered
			//read alignent records
			while (line != null){
				String[] separated = line.split("\\s+");	//split tags			

				ContigData cd;
				if (separated[2].equals(separated[0])){//the target and the candidate are the same (alignment score threshold comes from reference contig against itself)		
					cd	= new ContigData(separated[2],separated[0],Integer.parseInt(separated[11].substring(5, separated[11].length())),Integer.parseInt(separated[17].substring(5, separated[17].length())));
					aScoreRef=(int) (cd.as*ASthresholdPERCENT/100);//store 90% of alignment score as threshold

				}else{	//				
					cd	= new ContigData(separated[2],separated[0],Integer.parseInt(separated[11].substring(5, separated[11].length())),Integer.parseInt(separated[17].substring(5, separated[17].length())));
					if (cd.as>aScoreRef){
						contigsList.put(cd.getContigName(), cd);
					}
				}
				line = br.readLine();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		header=">CONTIG_NAME"+"\t"+"CONTIG_QUERY"+"\t"+"ALGN_SC"+"\t"+"NM(EDIT_DISTANCE) "+"\t\t"+"<AS threshold: "+aScoreRef+"("+ASthresholdPERCENT+"%"+" of "+(aScoreRef*100/ASthresholdPERCENT)+" reference)>"+"\n";


	}

	public String getHeader(){
		return header;
	}

	public String printContigList() {
		
		String outString="";
		
		for (Map.Entry<String, ContigData> entry : contigsList.entrySet()) {
		  String key = entry.getKey();
		  ContigData value = entry.getValue();
		  outString+=key+"\t"+value.candidateContig+"\t"+value.getAs()+"\t"+value.getNm()+"\n";
		  // do stuff
		}
		return outString;
	}
	
	private File createNewFile(String dirName,String fileName){


		//dirName="c:\\dir1\\dir2";
	    //fileName="fileName.txt";
	    File file = new File(dirName + "/" + fileName);
	    try {
	        new File(dirName).mkdirs();   // directory created here
	        file.createNewFile();  // file created here
	        return file;
	    }catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	            return null;
	         }
		}

}