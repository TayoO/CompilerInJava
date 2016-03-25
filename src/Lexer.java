//Oct.6, 1998   by Gregor v. Bochmann & Yaoping Wang
// revised by G.v. Bochmann, March 7, 2009
import java.io.*;
import java.util.*;

public class Lexer {
	public final int BEGIN = 1;
	public final int END = 2;
	public final int ASSIGN = 3;
	public final int PLUS = 4;
	//public final int MINUS = 5;
	public final int IDENT = 6;
	public final int SEMICOLON = 7;
	public final int EOF = 8;
	public final int IF =9;
	public final int BOOLEAN=10;
	public final int OPENCURL= 11;
	public final int CLOSECURL= 12;
	public String idName;  // identifier name
	public String booleanExp;
	public int token;  //holds the next token
	private boolean endOfFile; // whether end of file has been encountered
    
	private BufferedReader input; // input file buffer
	private char c;  //holds the next character

	public Lexer(String inputFile){
		try{input = new BufferedReader(new FileReader(inputFile));} 
		catch(IOException ee) {ee.printStackTrace();}
	}

	public void start() throws IOException {
		try {nextChar(); getNextToken();}
			catch (EndOfFileEncountered e) {token = EOF;}
	}

	public void getNextToken()throws IOException {
		if (endOfFile){token = EOF; return;} 
		String terminalString ="";
		try {

			 if(Character.isLetter(c)){ //first character is a letter, get whole alphanumeric string
				 if (c == 'i') {// check that next char is 'f' to find if statement token "if"
						nextChar();
						disposeSpace();
						if (c == 'f') {token = IF;}
						nextChar();
						disposeSpace();
							if (c== '{') {token = OPENCURL;}
							nextChar();
							disposeSpace();
						    if (Character.isLetterOrDigit(c))
						    {
						    	appendNextID("if {");
						    	nextChar();
						    	disposeSpace();
						    	if (c=='=' )
						    		terminalString+=c;
						    		nextChar();
						    	    disposeSpace();
								    if (Character.isLetterOrDigit(c))
								    {
								    	appendNextID(terminalString);
								    	nextChar();
								    	disposeSpace();
								    	if (c=='}')
								    	{
								    		terminalString+='}';
			
								    	}
								    }
						    }
							

				 else{
				 terminalString += c;
				nextChar();
				while(Character.isLetterOrDigit(c)){terminalString += c; nextChar();}
				idName = terminalString;
				token = checkKeywords(terminalString);
				} 
			 }
			else if (c == '+') {token = PLUS; nextChar();} 
			// if (c == '-') {token = MINUS; nextChar();} 
			else if (c == ';') {token = SEMICOLON; nextChar();} 
			}
			else if (c == ':') { //check that next char is '=' to find assignment token ":=" 
						nextChar(); 
						if (c == '=') {token = ASSIGN; nextChar();}
						else {System.out.println("lexical error: '=' expected after ':'; skip to end of program");
						       skipToEndOfFile();}
			}           
		
			else {System.out.println("invalid lexical unit; skip to end of program"); skipToEndOfFile();}
			
		} catch (EndOfFileEncountered e) {
			endOfFile = true;
			token = (terminalString == "")? EOF : checkKeywords(terminalString);
			}
		}
	void appendNextID(String terminalString)throws IOException{

		try{
		while(Character.isLetterOrDigit(c)){terminalString += c; nextChar();}
		idName = terminalString;
		token = checkKeywords(terminalString);
		if (token ==IDENT)
		return;
		} catch (EndOfFileEncountered e) {
			endOfFile = true;
			token = (terminalString == "")? EOF : checkKeywords(terminalString);
		}
	}
	int checkKeywords(String s) {
		if(s.equals("BEGIN")) return(BEGIN);
		else if(s.equals("END")) return(END);
		else return(IDENT);
		}
	
	 void disposeSpace() throws IOException, EndOfFileEncountered{
		//get rid of all spaces like \t, \n, and blank space
		while(Character.isWhitespace(c)) {nextChar();}
		}

	 void nextChar() throws IOException, EndOfFileEncountered{//get next character
		int i;
		if ((i = input.read()) == -1)throw new EndOfFileEncountered();
		c = (char) i;
		System.out.print(c);
		}
	
	 void skipToEndOfFile() throws IOException, EndOfFileEncountered {
		while (true) {nextChar();}
		}
	
}

