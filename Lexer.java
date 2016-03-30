//Oct.6, 1998   by Gregor v. Bochmann & Yaoping Wang
// revised by G.v. Bochmann, March 7, 2009
import java.io.*;
import java.util.*;

public class Lexer {
	int counter=0;
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
	public String boolVal1;
	public boolean equals;
	public String boolVal2;
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
			disposeSpace();
 //System.out.println("c="+c);
			 if(Character.isLetter(c)){ //first character is a letter, get whole alphanumeric string
				
				 terminalString += c;
					nextChar();
					while(Character.isLetterOrDigit(c)){terminalString += c; nextChar();}
					idName = terminalString;
					token = checkKeywords(terminalString);
			 }			
							

			
		
			else if (c == '+') {token = PLUS; nextChar();} 
			// if (c == '-') {token = MINUS; nextChar();} 
			else if (c == ';') {token = SEMICOLON; nextChar();} 
			else if (c == ':') { //check that next char is '=' to find assignment token ":=" 
						nextChar(); 
						if (c == '=') {token = ASSIGN; nextChar();}
						else {System.out.println("lexical error: '=' expected after ':'; skip to end of program");
						       skipToEndOfFile();}
			}       
			else if (c=='{') {token = CLOSECURL; nextChar();} 
			else if (c=='}') {token = OPENCURL; nextChar();} 
			else if (c=='(')
			{
				
				
				nextChar();
				disposeSpace();
				appendNextID("");
				boolVal1=terminalString;

				nextChar();
				disposeSpace();
				if (c=='=')
				{
					equals=true;
				
					
					nextChar();
					disposeSpace();
					appendNextID("");
					boolVal2=terminalString;
					
					nextChar();
					disposeSpace();
					if (c==')')
						{
						token=BOOLEAN;
						}
				}
				else if (c=='!')
				{
					
					nextChar();
					disposeSpace();
						if (c=='=')
						{ equals=false;
						
						nextChar();
						disposeSpace();
						appendNextID("");
						boolVal2=terminalString;
						
						nextChar();
						disposeSpace();
						if (c==')')
							{
							token=BOOLEAN;
							}
						}
				}
					
			}
			else {System.out.println("invalid lexical unit; skip to end of program"); skipToEndOfFile();}
			
		} catch (EndOfFileEncountered e) {
			endOfFile = true;
			token = (terminalString == "")? EOF : checkKeywords(terminalString);
			}
		System.out.println("token= "+token +" idName"+idName);
		
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
	int checkKeywords(String s){
		//System.out.println("s="+s);
		if(s.equals("BEGIN")) return(BEGIN);
		else if(s.equals("END")) return(END);
		else if(s.equals("IF")|| s.equals("if")) return(IF);
		else return(IDENT);
		}
	
	 void disposeSpace() throws IOException, EndOfFileEncountered{
		//get rid of all spaces like \t, \n, and blank space
		while(Character.isWhitespace(c)) 
		{nextChar();
		counter++;
		}
		System.out.println("whitespaces"+counter);
		counter=0;
		}

	 void nextChar() throws IOException, EndOfFileEncountered{//get next character
		int i;
		if ((i = input.read()) == -1)throw new EndOfFileEncountered();
		c = (char) i;
		//System.out.print(c);
		}
	
	 void skipToEndOfFile() throws IOException, EndOfFileEncountered {
		while (true) {nextChar();}
		}
	
}

