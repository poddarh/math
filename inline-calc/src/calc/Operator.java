package calc;

public class Operator implements Term{
	private char operator;

	public char getOperator() {
		return operator;
	}

	public int getLevel() {
		switch (operator) {
		case '^':
			return 3;
		case '*':
		case '/':
			return 2;
		case '+':
		case '-':
			return 1;
		default:
			return 0;
		}
	}

	public void setOperator(char operator) {
		this.operator = operator;
	}

	public Operator(char operator) {
		this.operator = operator;
	}

	public double operate(Operand val1, Operand val2) throws Exception {
		double a = val1.getValue();
		double b = val2.getValue();

		switch (operator) {
		case '^':
			return Math.pow(a, b);
		case '*':
			return a * b;
		case '/':
			if(b==0)
				throw new Exception("Math Error");
			return a / b;
		case '+':
			return a + b;
		case '-':
			return a - b;
		default:
			return 0.0;
		}
	}

	@Override
	public String toString() {
		return String.valueOf(operator);
	}

	@Override
	public boolean equals(Object obj) {
		if (Operator.class.isInstance(obj)) {
			return operator == ((Operator) obj).getOperator();
		} else
			return false;
	}

}
