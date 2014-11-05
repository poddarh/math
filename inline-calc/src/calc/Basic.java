package calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Basic {

	public static void main(String[] args){

		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		input = Operand.getValuesOfConstants(input);
		input = Function.convertToIdentifiers(input);
		input = input.replaceAll("\\s", "");
		
		List<Object> list = null;
		try {
			list = morph(input);
		} catch (Exception e) {
			System.out.println("Syntax Error");
			in.close();
			return;
		}
		System.out.println(list);
		if(Validator.validateSyntax(list)){
			try {
				solve(list);
			} catch (Exception e) {
				System.out.println("Math Error");
				in.close();
				return;
			}
			System.out.println(Float.valueOf(list.get(0).toString()));
		}else
			System.out.println("Syntax Error");

		
		in.close();

	}
	
	private static List<Object> solve(List<Object> list) throws Exception {
		for (int currentLevel = 5; currentLevel > 0 ; currentLevel--) {
			for (int i = 0; i < list.size(); i++) {
				Object term = list.get(i);
				if(currentLevel==5 && List.class.isInstance(term)){
					@SuppressWarnings("unchecked")
					List<Object> subList = (List<Object>) term;
					subList = solve(subList);
					if(subList.size()==1)
						list.set(i, subList.get(0));
					else
						list.set(i, subList);
						
				}
				else if(currentLevel==4  && Function.class.isInstance(term)){
					Function function = (Function)term;
					list.set(i, new Operand(function.solve((Operand) list.get(i+1))));
					list.remove(i+1);
				}
				else if (Operator.class.isInstance(term)) {
					Operator operator = (Operator) term;
					if(operator.getLevel()==currentLevel){
						double result = operator.operate((Operand)list.get(i-1), (Operand)list.get(i+1));
						list.remove(i-1);
						list.remove(i-1);
						list.set(i-1, new Operand(result));
						i--;
					}
				}
			}
		}
		return list;
	}
	
	private static List<Object> morph(String input) throws Exception {
		List<Object> list = new ArrayList<>();
		
		String str = "";
		int bracStart = -1;
		int freq = 0;
		
		for (int i = 0; i < input.length(); i++) {
		
			char ch = input.charAt(i);
			
			if(ch == '('){
				if(!str.equals("")){
					list.add(new Operand(str));
					list.add(new Operator('*'));
					str="";
				}
				if(bracStart == -1)
					bracStart=i;
				freq++;
			}
			
			else if(ch == ')'){
				freq--;
				if(freq==0){
					if(list.size()>0 && (Operand.class.isInstance(list.get(list.size()-1)) || List.class.isInstance(list.get(list.size()-1))))
						list.add(new Operator('*'));
					list.add(morph(input.substring(bracStart+1,i)));
					bracStart=-1;
				}
			}
			
			if(bracStart==-1){
				if(ch >= 'a' && ch<= 'h'){
					if(!str.equals("")){
						list.add(new Operand(str));
						list.add(new Operator('*'));
						str="";
					}
					list.add(new Function(ch));
				} else if ((ch >= '0' && ch <= '9') || ch == '.') {
					if(str.contains(".") && ch == '.'){
						throw new Exception("Syntax Error");
					}
					str += ch;
				} else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
					if((ch=='+' || ch=='-') && str.length()==0 && (list.size()==0 || Operator.class.isInstance(list.get(list.size()-1)))){
						str+=ch;
					}else{
						if(!str.equals(""))
							list.add(new Operand(str));
						list.add(new Operator(ch));
						str = "";
					}
				} else if(ch == '(' && !str.equals("")){
					list.add(new Operand(str));
					list.add(new Operator('*'));
					str="";
				} else if(ch!='(' && ch!=')'){
					throw new Exception("Syntax Error");
				}
			}
		}
		if (!str.equals("")) {
			list.add(new Operand(str));
		}
		return list;
	}

}
