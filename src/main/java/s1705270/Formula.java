package s1705270;

import java.util.Set;

public abstract class Formula {

	/*
	 * private static String getConnective(int tokenID) { String literal =
	 * RCLexer.VOCABULARY.getLiteralName(tokenID); return literal.replaceAll("'",
	 * ""); }
	 */

	public static enum Type {
		PREDICATE(""), COMPARISON(""), NEGATION("~"), // Formula.getConnective(RCLexer.NEGATION)),
		CONJUNCTION("&"), // Formula.getConnective(RCLexer.CONJUNCTION)),
		DISJUNCTION("|"), // Formula.getConnective(RCLexer.DISJUNCTION)),
		IMPLICATION("->"), // Formula.getConnective(RCLexer.IMPLICATION)),
		UNIVERSAL("[A]"), // Formula.getConnective(RCLexer.UNIVERSAL)),
		EXISTENTIAL("[E]");// Formula.getConnective(RCLexer.EXISTENTIAL));

		private final String connective;

		private Type(String connective) {
			this.connective = connective;
		}

		public String getConnective() {
			return connective;
		}
	}

	private final Type type;

	public Formula(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public abstract Set<Term> free();

}
