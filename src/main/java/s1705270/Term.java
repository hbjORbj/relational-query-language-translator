package s1705270;

public class Term {
	private String value; // can be a constant value or variable name
	private Boolean constant; // tells whether or not "value" is a constant value; if not, it is a variable
								// name

	public Term(String value, boolean constant) {
		this.value = value;
		this.constant = constant;
	}

	public boolean isConstant() {
		return constant;
	}

	public boolean isVariable() {
		return !constant;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return constant ? value : '?' + value;
	}

	@Override
	public boolean equals(Object o) { // TODO: fix
		if (o instanceof Term == false) {
			return false;
		}
		Term t = (Term) o;
		if (value.equals(t.value) == false) {
			return false;
		}
		if (this.isConstant() != t.isConstant()) {
			return false;
		}
		return true;
	}
}