package calc;

public class Function implements Term {
	public static final char    SIN = 'a';
	public static final char    COS = 'b';
	public static final char  COSEC = 'c';
	public static final char    SEC = 'd';
	public static final char    TAN = 'e';
	public static final char    COT = 'f';
	public static final char    LOG = 'g';
	public static final char     LN = 'h';
	public static final char RANDOM = 'i';
	
	private char function;
	public char getFunction() {
		return function;
	}
	public void setFunction(char function) {
		this.function = function;
	}

	public static String convertToIdentifiers(String input) {
		input=input.replaceAll("sin", String.valueOf(SIN));
		input=input.replaceAll("cos", String.valueOf(COS));
		input=input.replaceAll("cosec", String.valueOf(COSEC));
		input=input.replaceAll("sec", String.valueOf(SEC));
		input=input.replaceAll("tan", String.valueOf(TAN));
		input=input.replaceAll("cot", String.valueOf(COT));
		input=input.replaceAll("log", String.valueOf(LOG));
		input=input.replaceAll("ln", String.valueOf(LN));
		input=input.replaceAll("ran", String.valueOf(RANDOM));
		return input;
	}
	
	public Function(char function) {
		this.function = function;
	}
	
	public double solve(Operand val) {
		double a = val.getValue();
		switch (function) {
		case SIN:
			return Math.sin(a);
		case COS:
			return Math.cos(a);
		case TAN:
			return Math.tan(a);
		case COSEC:
			return 1/Math.sin(a);
		case SEC:
			return 1/Math.cos(a);
		case COT:
			return 1/Math.tan(a);
		case LOG:
			return Math.log10(a);
		case LN:
			return Math.log(a);
		case RANDOM:
			return Math.random()*a;
		}
		return 0.0;
	}
	
	@Override
	public String toString() {
		return String.valueOf(function);
	}
	
}
