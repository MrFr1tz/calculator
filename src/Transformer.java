
public class Transformer {
	static public String deleteSpaces(String str){
		str = str.replace(" ", "");
		return str;
	}
	
	static public String replace(String exp, int[] bounds, String result){
		StringBuffer sb = new StringBuffer(exp);
		sb.replace(bounds[0], bounds[1], result);
		return sb.toString();
	}
}
