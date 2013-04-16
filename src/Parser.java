package ru.kstovo.calculator.engine;



public class Parser {
	private Expression expression = null;
	private String exp = null;
	private int RightBracketPos = 0;
	private int LeftBracketPos = 0;
	private int CurrentOperationPosition = 0;
	private int LastExpressionRightBound = 0;
	private int LastExpressionLeftBound = 0;
	public boolean calculated = false;
	final private static char[] OPERATOR = {'*','/','+','-'}; 
	
	/*Constructor*/
	public Parser(Expression exp){
		this.expression = exp;
		this.exp = expression.getInputExpression();
	}
	
	/*Wrapper for following methods checkBrackets() && checkExpression()*/
	public boolean checkCorrectness(){
		this.exp = Transformer.resolveOperation(this.exp);
		if( this.exp.length() == 0 ){
			return false;
		}
		this.expression.setNewExpression(this.exp);
		return checkBrackets() && checkExpression(); 
	}
	
	/*Clarification for dots*/
	private boolean checkDots(){
		char c;
		int counter;
		
		for ( int i = 0 ; i < exp.length(); i++ ){
			c = exp.charAt(i);
			if( c == '.' ){
				counter = i + 1;
				while( counter < exp.length() && 
						( c != '+' && 
						  c != '-' &&
						  c != '*' &&
						  c != '/' ) ){
					c = exp.charAt(counter);
					if( c == '.'){
						return false;
					}
					counter++;
				}
			}
		}
		
		return true;
	}
	
	/*Clarification for brackets*/
	private boolean checkBrackets(){
		int LeftBrackets = 0;
		int RightBrackets = 0;
		int i = 0;
		int pairs = 0;
		
		if( this.exp == null ){
			return false;
		}
		
		for( i = exp.length() - 1; i >= 0 ; i-- ){
			char c = exp.charAt(i);
			if( ( c == '(' ) &&  ( pairs == 0 ) ){
				System.out.println("Opened '(' bracket without closed bracket ')'");
				//System.out.println(LeftBrackets + " " + RightBrackets + " " + pairs);
				return false;
			}
			else if ( c == '(' && ( pairs != 0 ) ){
				LeftBrackets++;
				pairs--;
			}
			
			if ( c == ')' && i > 0  && '(' != exp.charAt(i - 1) ){
				RightBrackets++;
				pairs++;
			}
			else if( c == ')' && i > 0 && '(' == exp.charAt(i - 1) ){
				return false;
			}
			
			if(  ( c == ')' ) && exp.length() == 1 ){
				return false;
			}
		}
		
		if( LeftBrackets == RightBrackets &&
				pairs == 0 )
			return true;
		
		System.out.println("Closed ')' bracket without opened bracket '('");
		//System.out.println(LeftBrackets + " " + RightBrackets + " " + pairs);
		return false;
	}
	
	/*Clarification for input expression*/
	private boolean checkExpression(){
		char c;
		char lchar = 0;
		char rchar = 0;
		boolean isDot = false;
		boolean retval = false;
		
		for ( int i = 0; i < OPERATOR.length; i++ ){
			for ( int j = 0; j < exp.length(); j++ ){
				c = exp.charAt(j);
				
				if( (c < '0' || c > '9') && 
					 c != '+' && 
					 c != '-' && 
					 c != '*' && 
					 c != '/' &&
					 c != '.' &&
					 c != '(' &&
					 c != ')'){
					return retval;
				}
				
				if( OPERATOR[i] == exp.charAt(j) ){
					if( j == 0 && OPERATOR[i] == '-' ){
						rchar = exp.charAt(j+1);
						if( ('0' <= rchar && '9' >= rchar) ||
								rchar == '('){
							continue;
						}
					}
					else if( j > 0 ){
						lchar = exp.charAt(j-1);
						if( j + 1 < exp.length() )
							rchar = exp.charAt(j+1);	
					}
					else{
						System.out.println("Expression could't start with " + OPERATOR[i] + " operator"); 
						return retval;
					}
					
					if( ( ( '0' <= lchar && '9' >= lchar ) || lchar == ')' ||
						lchar == '*' || lchar == '/' || lchar == '(' )  &&
						( ( '0' <= rchar && '9' >= rchar) || 
						rchar == '(' || 
						( rchar == '-' && ( (j+2) < exp.length() ) && 
						( exp.charAt(j+2) >= '0' && exp.charAt(j+2) <= '9' ) ) ) ){
						continue;
					}
					
					else
					{
						System.out.println("Wrong expression.");
						return retval;
					}
				}
				else if( exp.charAt(j) == '.' ){
					if( j == 0 || j == exp.length() - 1){
						return retval;
					}
					lchar = exp.charAt(j-1);
					rchar = exp.charAt(j+1);
					isDot = true;
					if( lchar < '0' || 
						lchar > '9' ||
						rchar < '0' || 
						rchar > '9' ){
						return retval;
					}
				}
			}
		}
		
		retval = true;
		
		if( isDot == true ){
			retval = this.checkDots();
		}
		return retval;
	}
	
	/*Return operands and sign of operation*/
	public String[] getNextExpression(){
		String[] operands = null;
		char operation = getMostPriorityOperation();
		if( operation == ' ' ){
			calculated = true;
			operands = new String[1];
			operands[0] = this.exp;
			return operands;
		}
		operands = getOperandsForCurrentOperation();
		operands[2] = Character.toString(operation);
		return operands;
	}
	
	/*Get the most priority operator. Wrapper for getNextOperation() method*/
	private char getMostPriorityOperation(){
		String str = getExpressionInBrackets();
		if(str != null){
			return getNextOperation(str);
		}
		return getNextOperation(this.exp);
	}
	
	/*Find the most deep expression in brackets*/
	private String getExpressionInBrackets(){
		this.LeftBracketPos = 0;
		this.RightBracketPos = 0;
		for (int i = 0; i < this.exp.length(); i++){
			if( '(' == this.exp.charAt(i)){
				this.LeftBracketPos = i;
			}
			
			if( ')' == this.exp.charAt(i) ){
				this.RightBracketPos = i;
				break;
			}
		}
		if ( 0 != this.RightBracketPos || 0 != this.LeftBracketPos ){
			return this.exp.substring(this.LeftBracketPos, this.RightBracketPos+1);
		}
		else{
			return null;
		}
			
	}
	
	/*Get next operation in expression, in accordance with priority*/
	private char getNextOperation(String str){
		char operation;
		
		for ( int i = 0; i < str.length(); i++){
			operation = str.charAt(i);
			if( operation == '*' || operation == '/'){
				this.CurrentOperationPosition = i + this.LeftBracketPos;
				return operation;
			}
		}
		
		for ( int i = 0; i < str.length(); i++){
			operation = str.charAt(i);
			if( ( operation == '+' && i != 0 ) || (operation == '-' && i != 0 && str.charAt(i - 1) != '(' ) ){
				this.CurrentOperationPosition = i + this.LeftBracketPos;
				return operation;
			}
		}
		return ' ';
	}
	
	/*Get left operand*/
	private String getLeftOperand(){
		int pointer = this.CurrentOperationPosition;
		int currentPos = pointer - 1;
		
		while( (currentPos > 0 && 
				'0' <= exp.charAt(currentPos) &&
				'9' >= exp.charAt(currentPos) &&
				'(' != exp.charAt(currentPos - 1)) || 
				'.' == exp.charAt(currentPos) ||
				'E' == exp.charAt(currentPos) ){
			currentPos--;
		}

		this.LastExpressionLeftBound = currentPos;
		if(exp.charAt(this.LastExpressionLeftBound) == '+' ||
			exp.charAt(this.LastExpressionLeftBound) == '*' || 
			exp.charAt(this.LastExpressionLeftBound) == '/'){
			this.LastExpressionLeftBound++;
		}
		
		/*Dirty hack*/
		if( currentPos > 0 && 
				exp.charAt(currentPos - 1)=='('){
			this.LastExpressionLeftBound--;
		}
		
		return exp.substring(this.LastExpressionLeftBound, this.CurrentOperationPosition); 
	}
	
	/*Get right operand*/
	private String getRightOperand(){
		int pointer = this.CurrentOperationPosition;
		int currentPos = pointer + 1;
		
		while( currentPos < exp.length() ){
			if( ('0' <= exp.charAt(currentPos) && 
				'9' >= exp.charAt(currentPos)) || 
				'.' == exp.charAt(currentPos) ||
				'E' == exp.charAt(currentPos) ){
				currentPos++;
			}
			else if( exp.charAt(currentPos) == '-' &&
				( '*' == exp.charAt(currentPos - 1) || 
				'/' == exp.charAt(currentPos - 1) ) ){
				currentPos++;
			}
			else{
				break;
			}
		}
		
		this.LastExpressionRightBound = currentPos;
		/*Dirty hack*/
		if( currentPos < exp.length() && 
			exp.charAt(currentPos)==')'){
			this.LastExpressionRightBound++;
		}
		
		return exp.substring(this.CurrentOperationPosition + 1, this.LastExpressionRightBound);
	}
	
	/*Find and return array with operands */
	private String[] getOperandsForCurrentOperation(){
		String[] operands = new String[3];
		
		operands[0] = getLeftOperand();
		operands[1] = getRightOperand();
		
		return operands;
	}

	/*Get bounds for last found expression for further expression modification*/
	public int[] getBoundsForLastExpression(){
		int[] bounds = new int[2];
		
		bounds[0] = this.LastExpressionLeftBound;
		bounds[1] = this.LastExpressionRightBound;
		
		return bounds;
	}
	
	/*Save modified expression*/
	public boolean passNewString(String s){
		if(s == null){
			return false;
		}
		this.expression.setNewExpression(Transformer.resolveOperation(s));
		this.exp = this.expression.getInputExpression();
		return true;
	}
}