import java.lang.Integer;
//a class for the purposes of organizing and running the computations for plagiarism likelihood
public class Driver {
	public static void main(String[] args){
		try{
			//n was provided, use that n
			if(args.length >= 3){
				PlagiarismClassifier pc;
				if(args.length == 4){
					int n = Integer.parseInt(args[3]);
					//create new classifier object with provided n
					pc = new PlagiarismClassifier(args[1],args[2],args[0],n);
				}
				else{
					//create new classifier object with default n
					pc = new PlagiarismClassifier(args[1],args[2],args[0]);
				}
				//train the model i.e. prepare for tuple comparisons
				pc.train();
				//predict likelihood of plagiarism i.e. compute ratio of tuple equivalences
				int percent = (int)(pc.pred()*100);
				//print the results to the console
				System.out.println(percent+"%");
			}
			else{
				System.out.println("ERROR! Insufficient number of parameters.");
				throw new Exception();
			}
		}
		//instructions
		catch(Exception e){
			System.out.println("\nPlease use the program per the following instructions: \nFirst compile the Driver.java file with the command \"javac Driver.java\"");
			System.out.println("This should compile the three java files producing three class files: Driver.class, Synonym.class, and PlagiarismClassifier.class. \nThe Driver.class is the program you need to use");
			System.out.println("Driver.class will take two files of writing samples and a file of synonyms and produce a percent confidence that one of the works was plagiarized from the other.");
			System.out.println("Optionally, a fourth integer parameter may be provided. This defines the size of the tuples to be used to analyze the two text samples");
			System.out.println("Make sure the three files are in the same folder as Driver.class then initiate the program by typing \"java Driver *synonyms* *sample1* *sample2* *OPTIONALLY n*\"");
			System.out.println("Where *sample1* is the name of the first writing sample file, *sample2* is the name of the second writing sample file, and \n*synonym* is the name of the file containing the dictionary of synonyms.");
		}
	}
}
