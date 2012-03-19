
public class Transformer {
	static public String deleteSpaces(String str){
		str = str.replace(" ", "");
		return str;
	}
	
	static public String replace(String exp, int[] bounds, int result){
		StringBuffer sb = new StringBuffer(exp);
		sb.replace(bounds[0], bounds[1], String.valueOf(result));
		return sb.toString();
	}
}
