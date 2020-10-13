package honours_project;

public class Term {
	private String value;
	private boolean constant;

	public Term (String value, boolean constant) {
		this.value = value;
		this.constant = constant;
	}

	public boolean isConstant() {
		return constant;
	}

	public boolean isAttribute() {
		return !constant;
	}

	public String getValue() {
		return value;
	}
}
