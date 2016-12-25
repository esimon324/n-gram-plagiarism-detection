//This is a class representing a synonym. Each synonym belongs to a class of synonym defined by its representative member.
//The representative member is arbitrarily chosen to be the first member in the list given by the synonym file.
//This class stores the member and the synonym class it belongs to denoted by the rep string
public class Synonym implements Comparable<Synonym> {
	private String rep; //the primary representative for this synonym class
	private String member; //a string member of the synonym congruence class defined by rep
	
	//set instance variables
	public Synonym(String rep, String member){
		this.rep = rep;
		this.member = member;
	}
	
	//set instance variables WITHOUT rep field; used for purposes of searching in PlagiarismClassifier
	public Synonym(String member){
		this.rep = "";
		this.member = member;
	}

	//Synonyms are ordered by alphabetical order of their member field
	public int compareTo(Synonym s) {
		return this.member.compareTo(s.member);
	}
	
	//overriding default toString method
	public String toString(){
		return "("+rep+","+member+")";
	}
	
	//Getter methods
	public String getRepresentative(){
		return this.rep;
	}
	
	public String getMember(){
		return this.member;
	}
}
