
public class Expression {

	private String expression;
	
	Expression(String exp){
		this.expression = exp;
	}
	
	public String getInputExpression(){
		return this.expression;
	}
	
	public boolean setNewExpression(String str){
		if(str == null){
			return false;
		}
		this.expression = str;
		return true;
	}
}
