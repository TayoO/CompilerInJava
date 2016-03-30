// Syner.java Oct.6, 1998   By Gregor v. Bochmann & Yaoping Wang
// revised by G.v. Bochmann, March 7, 2009, and March 4, 2013
import java.io.*;
import java.util.*;

public class Syner {
	private Lexer lex;
	private Hashtable symbolTable;
	
	public Syner(String inputFile) {
		lex = new Lexer(inputFile);
		symbolTable = new Hashtable();
		symbolTable.put("zero", new Integer(0) );
		symbolTable.put("one", new Integer(1) );
		symbolTable.put("ten", new Integer(10) );
	}

	public void startAnalysis() throws IOException {
		lex.start(); //start lexical analyser to get a token
		parseProgram(); //call parseProgram() to process the analysis
		//after "END" token, there should be the EOF token
		if(lex.token == lex.EOF) {System.out.println("\n"+"analysis complete, no syntax error");}
		else {errorMessage("after END - more tokens before EOF");}			
	}

	public void parseProgram() throws IOException {
		System.out.println("program"+lex.token);
		if(lex.token == lex.BEGIN){

			while (true){
				lex.getNextToken();
				System.out.println("hellog"+lex.token);
				parseStatement();
				if(lex.token != lex.SEMICOLON){
					break;
				}
			}
			if (lex.token == lex.END) {lex.getNextToken(); }
			else {errorMessage("END token expected!"); }
		}
		else {
			errorMessage("BEGIN token expected!");
		}
	}
	
	public void parseStatement() throws IOException {
		System.out.println("parse: "+lex.token);
		if (lex.token == lex.IDENT) {
			String var = lex.idName;
			lex.getNextToken(); 

			if (lex.token == lex.ASSIGN) {
				lex.getNextToken(); 
				int v = parseExpression();
				symbolTable.put(var, new Integer(v) );
				
			} else {
				errorMessage("assignment symbol expected");
			}
		}
		else if (lex.token == lex.IF)
		{
			parseIf();
		}
		else {
			errorMessage("identifier or if expected at the begining of a statement");
		}
	}
	public void parseIf() throws IOException {
		lex.getNextToken();
		//should find the boolean expression
		if(lex.token == lex.BOOLEAN)
		{
			
		
		boolean test= parseBoolean();
		lex.getNextToken();
			if(lex.token == lex.OPENCURL)
			{
				if (test)
				{
					parseStatement();
				}
				else
				{
					int openMinusClosedCurl=1;
					while (openMinusClosedCurl!=0)
					{
						if (lex.token== lex.CLOSECURL)
						{
							System.out.println("{");
							openMinusClosedCurl++;
						}
						else if (lex.token=='}')
						{
							openMinusClosedCurl--;
							System.out.println("}");
						}
						lex.getNextToken();
					}
				}
		       	
			}
			
		}
	}
	
	public int parseExpression() throws IOException {
		if(lex.token == lex.IDENT) {
			int value = ((Integer)symbolTable.get(lex.idName)).intValue();
			lex.getNextToken();
			if(lex.token == lex.PLUS ) {
				boolean opIsPlus = (lex.token == lex.PLUS); // just to remember what the operation is
				lex.getNextToken();
				if(lex.token == lex.IDENT) {					
					int tmp = ((Integer)symbolTable.get(lex.idName)).intValue();
					value = (opIsPlus)? (value + tmp) : (value - tmp) ; 
					lex.getNextToken();
					return(value); }
				else {errorMessage("identifier expected at the end of the expression");
				return(0);}
			}
			else {return(value);}
		}
		else {errorMessage("identifier expected at the beginning of a expression");
		return(0);}
	}
	public boolean parseBoolean() throws IOException {
		int val1=(((Integer)symbolTable.get(lex.boolVal1)).intValue());
		int val2=(((Integer)symbolTable.get(lex.boolVal2)).intValue());
		return ((val1==val2)==lex.equals);
	}

	public void errorMessage(String s) throws IOException {
		System.out.println(s);
		// skip to the end of the program
		while (lex.token != lex.EOF) {lex.getNextToken();}
	}
}

