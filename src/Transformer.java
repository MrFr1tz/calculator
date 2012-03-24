
public class Transformer {
	
	/*Delete spaces from input expression*/
	static public String deleteSpaces(String str){
		str = str.replace(" ", "");
		return str;
	}
	
	/*Replace substring in specified bounds on other string*/
	static public String replace(String exp, int[] bounds, String result){
		StringBuffer sb = new StringBuffer(exp);
		sb.replace(bounds[0], bounds[1], result);
		return sb.toString();
	}
	
	/*Replace '+-' on '-' and '--' on '+' */
	public static String resolveOperation(String str){
		for(int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if( c == '+' && str.charAt(i+1)=='-'){
				str = str.replace("+-", "-");
			}
			else if( c == '-' && str.charAt(i+1)=='-'){
				str = str.replace("--", "+");
			}
		}	
		return str;
	}
	
	/*Delete unneeded brackets from expression*/
	private static String removeUnneededBrackets(String str){
		char c;
		int lbracketpos;
		StringBuffer sb =  new StringBuffer(str);
		
		for(int i = 0; i < sb.length(); i++){
			c = sb.charAt(i); 
			if( c == '(' && ( sb.charAt(i+1) == '-' || ( '0' <= sb.charAt(i+1) && '9' >= sb.charAt(i+1) ) ) ){
				lbracketpos = i;
				int counter = i + 1;
				while( c != ')' ){
					counter++;
					c = sb.charAt(counter);
					if( c == '-' || 
						c == '+' || 
						c == '*' || 
						c == '/' ){
						break;
					}
					
					if( c == ')'){
						sb.deleteCharAt(lbracketpos);
						sb.deleteCharAt(counter - 1);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/*Prepare the input expression to parse*/
	static public String prepareExpression(String str){
		char c;
		StringBuffer sb = new StringBuffer(str);
		
		for ( int i = 0; i < sb.length(); i++ ){
			c = sb.charAt(i);
			if( c == ')' && 
				( i+1 < sb.length() ) && 
				sb.charAt(i+1) == '('){
				sb.insert(i+1, "*");
			}
			else if( (c >= '0' && c <= '9') &&
				(i+1 < sb.length()) &&
				sb.charAt(i+1) == '(' ){
				sb.insert(i+1, "*");
			}
		}
	
		return Transformer.removeUnneededBrackets(sb.toString());
		//return sb.toString();
	}
	
	/*Prepare calculated result before output*/
	static public String prepareResultForOutput(String str){
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = str.replace("", "");
		str = str.replace("+", "");
		return str.replace(str, "=" + str);
	}
}
