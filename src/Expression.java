package ru.kstovo.calculator.engine;


public class Expression {

	private String expression;
	
	public Expression(String exp){
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
