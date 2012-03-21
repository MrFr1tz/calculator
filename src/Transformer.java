
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
	
	/*Prepare the input expression to parse*/
	static public String prepareExpression(String str){
		return str;
	}
}
