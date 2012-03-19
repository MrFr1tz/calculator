
public class Calculator {

	public static String evaluate(String operand1, String operand2, String operation){
		char operator = operation.charAt(0);
		if(operand1.charAt(0) == '(' ||
			operand2.charAt(operand2.length() - 1) == ')'){
			StringBuffer sb = new StringBuffer(operand1);
			sb.deleteCharAt(0);
			operand1 = sb.toString();
			
			sb = new StringBuffer(operand2);
			sb.deleteCharAt(operand2.length()- 1);
			operand2 = sb.toString();
		}
		
		int op1 = Integer.valueOf(operand1);
		int op2 = Integer.valueOf(operand2);
		switch (operator){
			case '+':
				return String.valueOf(op1+op2);
			case '-':
				return String.valueOf(op1-op2);
			case '*':
				return String.valueOf(op1*op2);
			case '/':
				if(op2 != 0){
					return String.valueOf(op1/op2);
				}
				else{
					System.out.println("Division by zero");
				}
			default:
				break;
		}
		return null;
	}
}
