
public class Parser {
	private Expression expression = null;
	private String exp = null;
	private int RightBracketPos = 0;
	private int LeftBracketPos = 0;
	private int CurrentOperationPosition = 0;
	private int LastExpressionRightBound = 0;
	private int LastExpressionLeftBound = 0;
	public boolean calculated = false;
	
	/*Constructor*/
	Parser(Expression exp){
		this.expression = exp;
		this.exp = expression.getInputExpression();
	}
	
	final private static char[] OPERANDS = {'*','/','+','-'}; 
	
	/*Wrapper for following methods checkBrackets() && checkExpression()*/
	public boolean checkCorrectness(){
		this.exp = resolveOperation(this.exp);
		this.expression.setNewExpression(this.exp);
		return checkBrackets() && checkExpression(); 
	}
	
	/*Clarification for brackets*/
	private boolean checkBrackets(){
		int LeftBrackets = 0;
		int RightBrackets = 0;
		int i = 0;
		int pairs = 0;
		
		if(this.exp == null){
			return false;
		}
		
		for( i = exp.length() - 1; i >= 0 ; i-- ){
			char c = exp.charAt(i);
			if( ( c == '(' ) &&  (pairs == 0) ){
				System.out.println("Opened '(' bracket without closed bracket ')'");
				//System.out.println(LeftBrackets + " " + RightBrackets + " " + pairs);
				return false;
			}
			else if ( c == '(' && (pairs != 0) ){
				LeftBrackets++;
				pairs--;
			}
			
			if ( c == ')' ){
				RightBrackets++;
				pairs++;
			}
		}
		
		if(LeftBrackets == RightBrackets &&
				pairs == 0)
			return true;
		
		System.out.println("Closed ')' bracket without opened bracket '('");
		//System.out.println(LeftBrackets + " " + RightBrackets + " " + pairs);
		return false;
	}
	
	/*Clarification for input expression*/
	private boolean checkExpression(){
		char lchar = 0;
		char rchar = 0;
		for ( int i = 0; i < OPERANDS.length; i++ ){
			for ( int j = 0; j < exp.length(); j++ ){
				if( OPERANDS[i] == exp.charAt(j) ){
					if( j == 0 && OPERANDS[i] == '-' ){
						rchar = exp.charAt(j+1);
						if( ('0' <= rchar && '9' >= rchar) ||
								rchar == '('){
							continue;
						}
					}
					else if( j > 0 ){
						lchar = exp.charAt(j-1);
						rchar = exp.charAt(j+1);	
					}
					else{
						System.out.println("Expression could't start with " + OPERANDS[i] + " operator"); 
						return false;
					}
					
					if( (('0' <= lchar && '9' >= lchar) || 
						lchar == ')' || lchar == '*') &&
						(('0' <= rchar && '9' >= rchar) || 
						rchar == '(' || rchar == '-') 
						){
						continue;
					}
					else
					{
						//System.out.println("Wrong expression.");
						return false;
					}
				}
			}
		}
		
		System.out.println("Good expression");
		return true;
	}
	
	/*Return operands and sign of operation*/
	public String[] getNextExpression(){
		String[] operands = null;
		char operation = getMostPriorityOperation();
		if(operation == ' '){
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
			if( operation == '+' || (operation == '-' && i != 0) ){
				this.CurrentOperationPosition = i + this.LeftBracketPos;
				return operation;
			}
		}
		return ' ';
	}
	
	/*Find and return array with operands */
	private String[] getOperandsForCurrentOperation(){
		int pointer = this.CurrentOperationPosition;
		int currentPos = pointer - 1;
		String[] operands = new String[3];
		
		while( currentPos != 0 && 
				'0' <= exp.charAt(currentPos) &&
				'9' >= exp.charAt(currentPos) &&
				'(' != exp.charAt(currentPos - 1)){
			currentPos--;
		}
		
		this.LastExpressionLeftBound = currentPos;
		if(exp.charAt(this.LastExpressionLeftBound) == '+' ||
			exp.charAt(this.LastExpressionLeftBound) == '*' || 
			exp.charAt(this.LastExpressionLeftBound) == '/'){
			this.LastExpressionLeftBound++;
		}
		
		/*Dirty hack*/
		if( currentPos != 0 && 
				exp.charAt(currentPos - 1)=='('){
			this.LastExpressionLeftBound--;
		}
		operands[0] = exp.substring(this.LastExpressionLeftBound, this.CurrentOperationPosition);
		
		/*---- Need proper solution for following situations '*-' '\-' ----*/ 
		currentPos = pointer + 1;
		while( currentPos < exp.length() ){
			if( ('0' <= exp.charAt(currentPos) && '9' >= exp.charAt(currentPos))){
				currentPos++;
			}
			else{
				break;
			}
		}
		
		/*Second dirty hack*/
		if( currentPos < exp.length() && 
			exp.charAt(currentPos)==')'){
			currentPos++;
		}
		this.LastExpressionRightBound = currentPos;
		operands[1] = exp.substring(this.CurrentOperationPosition + 1, currentPos);
		
		return operands;
	}

	/*Get bounds for last found expression for further expression modification*/
	public int[] getBoundsForLastExpression(){
		int[] bounds = new int[2];
		bounds[0] = this.LastExpressionLeftBound;
		bounds[1] = this.LastExpressionRightBound;
		return bounds;
	}
	
	/*Replace '+-' on '-' and '--' on '+' */
	private String resolveOperation(String str){
		for(int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if( c == '+' && str.charAt(i+1)=='-'){
				int[] bounds = new int[2];
				bounds[0] = i;
				bounds[1] = i+2;
				str = Transformer.replace(str, bounds, "-");
			}
			else if( c == '-' && str.charAt(i+1)=='-'){
				int[] bounds = new int[2];
				bounds[0] = i;
				bounds[1] = i+2;
				str = Transformer.replace(str, bounds, "+");
			}
		}
		return str;
	}
	
	/*Save modified expression*/
	public boolean passNewString(String s){
		if(s == null){
			return false;
		}
		this.expression.setNewExpression(resolveOperation(s));
		this.exp = this.expression.getInputExpression();
		return true;
	}
}