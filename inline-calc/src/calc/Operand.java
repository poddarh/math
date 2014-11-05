package calc;

public class Operand implements Term {
	private double value;

	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	public Operand(String str) {
		value = Double.valueOf(str);
	}
	public Operand(Double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	public static String getValuesOfConstants(String input){
		input=input.replaceAll("PI", "(" + Math.PI + ")");
		input=input.replaceAll( "E", "(" + Math.E + ")");
		return input;
	}
	
}
