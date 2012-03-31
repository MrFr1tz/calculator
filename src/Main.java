import java.util.Scanner;

public class Main {
		static public void main(String[] args){
			Scanner sc = new Scanner(System.in);
			String input = null;
			String[] str = null;
			
			while(sc.hasNext()){
				input = sc.nextLine();
				input = Transformer.deleteSpaces(input);
				input = Transformer.prepareExpression(input);
				
				if( input == null ){
					System.out.println("Expression is wrong");
					System.exit(1);
				}
				//break;
			
				Expression exp = new Expression(input);
				Parser parser = new Parser(exp);
			
				if( parser.checkCorrectness() == false ){
					System.out.println("Expression is wrong");
					System.exit(1);
				}

				System.out.println("Expression is right");
	
				while(true){ 
					str = parser.getNextExpression();
					if(true == parser.calculated){
						str[0] = Transformer.prepareResultForOutput(str[0]);
						System.out.println(str[0]);
						break;
						//System.exit(0);
					}
					int[] bounds = parser.getBoundsForLastExpression();
					String result = Transformer.replace(exp.getInputExpression(), bounds, Calculator.evaluate(str[0], str[1], str[2]));	
					if(result.compareTo("null") == 0){
						System.out.println("Division by zero");
						break;
					}
					if( false == parser.passNewString(result) ){
						System.out.println("Input string is empty.");
					}
				}
			}
		}
}
