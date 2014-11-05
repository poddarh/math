package calc;

import java.util.ArrayList;
import java.util.List;

public class Validator {

	private static List<Rule> rules = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public static boolean validateSyntax(List<Object> list) {
		for (int i = 0; i < list.size(); i++) {
			if(List.class.isInstance(list.get(i))){
				if(!validateSyntax((List<Object>) list.get(i)))
					return false;
			}
			else{
				Object left = i>0 ? list.get(i-1) : null;
				Object mid = list.get(i);
				Object right = i!=list.size()-1 ? list.get(i+1) : null;
				if(!doesObeyRule(left, mid, right))
					return false;
			}
		}
		return true;
	}
	
	private static boolean doesObeyRule(Object left, Object mid, Object right) {
		for (Rule rule : rules) {
			if(rule.matches(new Rule(left, mid, right)))
				return true;
		}
		return false;
	}	
	
	static{
		rules.add(new Rule(Operand.class, Operator.class, Operand.class));
		rules.add(new Rule(Operand.class, Operator.class, Function.class));
		
		rules.add(new Rule(null, Operand.class, null));
		rules.add(new Rule(null, Operand.class, Operator.class));
		rules.add(new Rule(Operator.class, Operand.class, null));
		rules.add(new Rule(Operator.class, Operand.class, Operator.class));
		rules.add(new Rule(Function.class, Operand.class, null));
		
		rules.add(new Rule(null, Function.class, Operand.class));
		rules.add(new Rule(Operator.class, Function.class, Operand.class));
	}
	
}
