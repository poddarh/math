package calc;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Rule {
	private Class left;
	private Class mid;
	private Class right;
	
	public boolean matches(Rule obj) {
		boolean isLeftSame = this.left == obj.left;
		boolean isMidSame = this.mid == obj.mid;
		boolean isRightSame = this.right == obj.right;
		return isLeftSame && isMidSame && isRightSame;
	}
	
	public Rule(Class left, Class mid, Class right) {
		this.left  = left  == ArrayList.class ? Operand.class :  left;
		this.mid   = mid   == ArrayList.class ? Operand.class :   mid;
		this.right = right == ArrayList.class ? Operand.class : right;
	}
	
	public Rule(Object left, Object mid, Object right) {
		this.left  = left==null ? null : (left.getClass() == ArrayList.class ? Operand.class :  left.getClass());
		this.mid   = mid==null ? null : (mid.getClass() == ArrayList.class ? Operand.class :   mid.getClass());
		this.right = right==null ? null : (right.getClass() == ArrayList.class ? Operand.class : right.getClass());
	}

	@Override
	public String toString() {
		return "Left: "+left.getSimpleName() + " | " + "Middle: "+mid.getSimpleName() + " | " + "Right: "+right.getSimpleName();
	}
	
}