import java.io.*;
import java.util.*;

//A class for the purposes of predicting the likelihood of plagiarism based on the resource files provided by the user
public class PlagiarismClassifier {
	private String fname1; //file name for the first writing sample
	private String fname2; //file name for the second writing sample
	private String synFname; //file name for the synonym library
	private String[] f1Tokens; //will contain the first writing sample as an array of tokens after training
	private String[] f2Tokens; //will contain the second writing sample as an array of tokens after training
	private int n; //size of the tuples used for comparision in prediction
	
	//instantiate relevant instances variables; user has provided value for size of tuples
	public PlagiarismClassifier(String fname1, String fname2, String synFname, int n){
		this.fname1 = fname1;
		this.fname2 = fname2;
		this.synFname = synFname;
		this.n = n;
	}
	
	//instantiate relevant instance variables using default value of n = 3
	public PlagiarismClassifier(String fname1, String fname2, String synFname){
		this.fname1 = fname1;
		this.fname2 = fname2;
		this.synFname = synFname;
		this.n = 3;
	}
	
	//Tokenizes writing samples and normalizes both texts so all synonyms appear as
	//the reprsentative of the synonym class as defined in Synonym.java
	public void train(){
		//Tokeinze and store contents of the three files
		ArrayList<Synonym> syns = this.buildSynonyms(this.synFname);
		Collections.sort(syns);
		this.f1Tokens = this.tokenize(fname1);
		this.f2Tokens = this.tokenize(fname2);
		
		int largerFileLength;
		if(this.f1Tokens.length > this.f2Tokens.length)
			largerFileLength = this.f1Tokens.length;
		else
			largerFileLength = this.f2Tokens.length;
		
		//iterate over all words in both files and convert to normalized form
		//normalized form has all synonyms converted to their representative member
		for(int i = 0; i < largerFileLength; i++){
			//Considering elements in the first sample writing
			if(i < this.f1Tokens.length){
				String token = this.f1Tokens[i];
				//binary search for synonym
				int index = Collections.binarySearch(syns, new Synonym(token));
				//if found, convert this synonym to its representative member
				if(index >= 0)
					this.f1Tokens[i] = syns.get(index).getRepresentative();
			}
			//Considering elements in the second sample writing
			if(i < f2Tokens.length){
				String token = this.f2Tokens[i];
				//binary search for synonym
				int index = Collections.binarySearch(syns, new Synonym(token));
				//if found, convert this synonym to its representative member
				if(index >= 0)
					this.f2Tokens[i] = syns.get(index).getRepresentative();;
			}
		}
	}
	
	//Compares n-tuples between writing samples as token arrays to determine probability of plagiarism
	public double pred() throws Exception{
		if(this.n <= this.f1Tokens.length && this.n <= this.f2Tokens.length)
		{
			double numTuplesEqual = 0;
			double numTuples = 0;
			int shorterFileLength;
			if(f1Tokens.length > f2Tokens.length)
				shorterFileLength = f2Tokens.length;
			else
				shorterFileLength = f1Tokens.length;
			
			//iterate over tuples in both writing samples
			for(int i = 0; i < shorterFileLength - n; i++)
			{
				//if tuples are equal, increment equality counter
				if(this.isEqualNTuples(i))
					numTuplesEqual++;
				numTuples++;
			}
			
			//define percent likelihood of plagiarism as number of same tuples over number of tuples
			return numTuplesEqual / numTuples;
		}
		else{
			System.out.println("ERROR! Parameter n is too large for the dataset provided. Please try again with smaller n.");
			throw new Exception();
		}
	}
	
	//Determines if two n-tuples starting at index start are the same
	private boolean isEqualNTuples(int start){
		boolean ret = true;
		for(int i = 0; i < n; i++){
			if(this.f1Tokens[start+i].compareTo(this.f2Tokens[start+i]) != 0)
				ret = false;
		}
		return ret;
	}
	
	//Converts the synonym file into a list of synonym objects
	private ArrayList<Synonym> buildSynonyms(String synFname){
		try {
			//read in contents of the file
			BufferedReader br = new BufferedReader(new FileReader(synFname));
			String line = br.readLine();
			String[] currentSynClass;
			ArrayList<Synonym> syns = new ArrayList<Synonym>();
			while(line != null){
				//split line by non-word characters to form tokens
				currentSynClass = line.toLowerCase().split("\\W");
				//for each token, form into a synonym object
				for(String s : currentSynClass)
					syns.add(new Synonym(currentSynClass[0],s));
				line = br.readLine();
			}
			br.close();
			return syns;
		} catch (IOException e) {
			System.out.println("ERROR! Synonym file \""+synFname+"\" was not found.");
			return null;
		}
	}
	
	//Takes in a file name and returns an array of lowercase tokens of the file's contents
	private String[] tokenize(String fname){
		try {
			//read in contents of file
			BufferedReader br = new BufferedReader(new FileReader(fname));
			String fileContents = "";
			String line = br.readLine();			
			while(line != null){
				//convert contents to lowercase
				fileContents += line.toLowerCase();
				line = br.readLine();
			}
			br.close();
			//create tokens from file delimited by non-word characters
			return fileContents.split("\\W");
		} catch (IOException e) {
			System.out.println("ERROR! Writing sample file \""+fname+"\" was not found.");
			return null;
		}
	}
}
