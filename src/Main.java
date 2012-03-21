import java.util.Scanner;

public class Main {
		static public void main(String[] args){
			Scanner sc = new Scanner(System.in);
			String input = null;
			String[] str = null;
			
			while(sc.hasNext()){
				input = sc.nextLine();
				input = Transformer.deleteSpaces(input);
				break;
			}
			
			Expression exp = new Expression(input);
			Parser parser = new Parser(exp);
			
			if(parser.checkCorrectness()==false){
				System.out.println("Wrong expression");
				System.exit(1);
			}
			
			/*Main cycle of application*/
			while(true){ 
				str = parser.getNextExpression();
				if(true == parser.calculated){
					System.out.println("=" + str[0]);
					System.exit(0);
				}
				int[] bounds = parser.getBoundsForLastExpression();
				String result = Transformer.replace(exp.getInputExpression(), bounds, Calculator.evaluate(str[0], str[1], str[2]));	
				if( false == parser.passNewString(result) ){
					System.out.println("Input string is empty.");
				}
			}
		}
}
