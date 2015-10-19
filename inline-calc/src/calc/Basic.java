package calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class Basic {

	public static void main(String[] args){

		HashMap<String, Double> variables = new HashMap<>();
		variables.put("ans", 0d);
		
		Scanner in = new Scanner(System.in);
		
		while(true){
			
			if(!in.hasNext())
				break;
			
			System.out.println();
			String input = in.nextLine();
			if(input.equals("exit"))
				break;
			
			input = input.replaceAll("\\s", "");
			
			String currentVariable = null;
			String expression = null;
			
			if(input.contains("=")){
				
				currentVariable = input.substring(0, input.indexOf('='));
				if(!currentVariable.matches("^[^0-9\\W][\\w$]*$")){
					System.out.println("Invalid variable name!");
					continue;
				}
				expression = input.substring(input.indexOf('=')+1);
				
			}
			else{
				expression = input;
			}
			
			expression = replaceVariableWithValues(variables, expression);
			expression = Operand.getValuesOfConstants(expression);
			expression = Function.convertToIdentifiers(expression);
			
			List<Object> list = null;
			try {
				list = morph(expression);
			} catch (Exception e) {
				System.out.println("Syntax Exception");
				continue;
			}
	//		System.out.println(list);
			if(Validator.validateSyntax(list)){
				try {
					solve(list);
				} catch (Exception e) {
					System.out.println("Math Error");
					continue;
				}
				
				Double value = Double.valueOf(list.get(0).toString());
				if(currentVariable != null){
					variables.put(currentVariable, value);
					System.out.print(currentVariable + " -> ");
				}
				else{
					variables.put("ans", value);
				}
				System.out.println(value.floatValue() + " ("+float2rat(value)+")");
				
			}
			else{
				System.out.println("Syntax Error");
			}
			
		}
		
		in.close();

	}

	private static String replaceVariableWithValues(HashMap<String, Double> variables, String input) {
		for(Entry<String, Double> entry : variables.entrySet()){
			input = input.replaceAll("(?<![a-zA-Z_$])"+ entry.getKey() +"(?![\\w$])", "(" + entry.getValue() + ")");
		}
		return input;
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
				if(ch >= 'a' && ch<= 'i'){
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
				} else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '%') {
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
	
	private static String float2rat(double x) {
		boolean isNegative = x < 0;
		if(isNegative)
			x = -x;
		
		double tolerance = 1.0E-6;
	    double h1=1; double h2=0;
	    double k1=0; double k2=1;
	    double b = x;
	    do {
	    	double a = Math.floor(b);
	    	
	    	double aux = h1;
	    	h1 = a*h1+h2;
	    	h2 = aux;
	    	
	        aux = k1;
	        k1 = a*k1+k2;
	        k2 = aux;
	        
	        b = 1/(b-a);
	        
	    } while (Math.abs(x-h1/k1) > x*tolerance);
	    
	    String h1String;
	    String k1String;
	    
	    if(h1%1==0)
	    	h1String = (int) h1+"";
	    else
	    	h1String = h1+"";
	    
	    if(k1%1==0)
	    	k1String = (int) k1+"";
	    else
	    	k1String = k1+"";
	    
	    if(isNegative)
	    	h1String = "-"+h1String;
	    	
	    return h1String+"/"+k1String;
	}
	
}
