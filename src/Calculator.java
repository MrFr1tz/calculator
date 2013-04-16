package ru.kstovo.calculator.engine;


public class Calculator {

	public static String evaluate(String operand1, String operand2, String operation){
		char operator = operation.charAt(0);
		
		if( operand1.charAt(0) == '('){
			StringBuffer sb = new StringBuffer(operand1);
			sb.deleteCharAt(0);
			operand1 = sb.toString();
		}
		
		if( operand2.charAt(operand2.length() - 1) == ')'){
			StringBuffer sb = new StringBuffer(operand2);
			sb.deleteCharAt(operand2.length() - 1);
			operand2 = sb.toString();
		}
		
		float op1 = Float.valueOf(operand1);
		float op2 = Float.valueOf(operand2);
		
		switch (operator){
			case '+':
				return String.valueOf(op1+op2);
			case '-':
				return String.valueOf(op1-op2);
			case '*':
				if(op1 < 0 && op2 < 0){
					return "+" + String.valueOf(op1*op2);
				}
				return String.valueOf(op1*op2);
			case '/':
				if(op2 != 0){
					if(op1 < 0 && op2 < 0){
						return "+" + String.valueOf(op1/op2);
					}
					return String.valueOf(op1/op2);
				}
				else{
					return "null";
					//System.out.println("Division by zero");
					//System.exit(0);
				}
			default:
				break;
		}
		return null;
	}
}
