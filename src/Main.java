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
			
			if(parser.checkCorrectness()){
				str = parser.getNextExpression();
				System.out.println(str[0] + " " + str[1] + " " + str[2]);
			}
			else{
				System.out.println("Wrong expression");
			}
				//break;
			//System.out.println(input);
		}
}
