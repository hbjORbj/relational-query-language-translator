package honours_project;

public abstract class Expression {
	
	private static String getConnective(int tokenID) {
		String literal = RCLexer.VOCABULARY.getLiteralName(tokenID);
		return literal.replaceAll("'", "");
	}

	public static enum Type {
		NEGATION		 (Expression.getConnective(RCLexer.NEGATION)),
		CONJUNCTION  (Expression.getConnective(RCLexer.CONJUNCTION)),
		DISJUNCTION  (Expression.getConnective(RCLexer.DISJUNCTION)),
		IMPLICATION  (Expression.getConnective(RCLexer.IMPLICATION)),
		UNIVERSAL    (Expression.getConnective(RCLexer.UNIVERSAL)),
		EXISTENTIAL  (Expression.getConnective(RCLexer.EXISTENTIAL));

		private final String connective;

		private Type ( String connective ) {
			this.connective = connective;
		}

		public String getConnective() {
			return connective;
		}
	}

	private final Type type;

	public Expression (Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
