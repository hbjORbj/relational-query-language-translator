package honours_project;

public abstract class Formula {
	
	private static String getConnective(int tokenID) {
		String literal = RCLexer.VOCABULARY.getLiteralName(tokenID);
		return literal.replaceAll("'", "");
	}

	public static enum Type {
		NEGATION		 (Formula.getConnective(RCLexer.NEGATION)),
		CONJUNCTION  (Formula.getConnective(RCLexer.CONJUNCTION)),
		DISJUNCTION  (Formula.getConnective(RCLexer.DISJUNCTION)),
		IMPLICATION  (Formula.getConnective(RCLexer.IMPLICATION)),
		UNIVERSAL    (Formula.getConnective(RCLexer.UNIVERSAL)),
		EXISTENTIAL  (Formula.getConnective(RCLexer.EXISTENTIAL));

		private final String connective;

		private Type ( String connective ) {
			this.connective = connective;
		}

		public String getConnective() {
			return connective;
		}
	}

	private final Type type;

	public Formula (Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
