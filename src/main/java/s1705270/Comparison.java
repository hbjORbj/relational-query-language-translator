package s1705270;

public abstract class Comparison extends Formula {

	/*
	 * private static String getConnective(int tokenID) { String literal =
	 * RCLexer.VOCABULARY.getLiteralName(tokenID); return literal.replaceAll("'",
	 * ""); }
	 */

	public static enum Type {
		EQUAL("="), // Formula.getConnective(RCLexer.EQUAL)),
		LESSTHAN("<");// Formula.getConnective(RCLexer.LESSTHAN)),
		// LESSTHANOREQUAL (Formula.getConnective(RCLexer.LESSTHANOREQUAL)),
		// GREATERTHAN (Formula.getConnective(RCLexer.GREATERTHAN)),
		// GREATERTHANOREQUAL (Formula.getConnective(RCLexer.GREATERTHANOREQUAL)),
		// NOTEQUAL (Formula.getConnective(RCLexer.NOTEQUAL));

		private final String connective;

		private Type(String connective) {
			this.connective = connective;
		}

		public String getConnective() {
			return connective;
		}
	}

	private final Type type;

	public Comparison(Type type) {
		super(Formula.Type.COMPARISON);
		this.type = type;
	}

	public Type getComparisonType() {
		return type;
	}
}
