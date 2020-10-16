package honours_project;

import honours_project.Expression.Type;

public abstract class Condition {

	private static String getConnective(int tokenID) {
		String literal = RCLexer.VOCABULARY.getLiteralName(tokenID);
		return literal.replaceAll("'", "");
	}

	public static enum Type {
		EQUAL		 	 	(Expression.getConnective(RCLexer.EQUAL)),
		LESSTHAN  	 	 	(Expression.getConnective(RCLexer.LESSTHAN)),
		LESSTHANOREQUAL  	(Expression.getConnective(RCLexer.LESSTHANOREQUAL)),
		GREATERTHAN  	 	(Expression.getConnective(RCLexer.GREATERTHAN)),
		GREATERTHANOREQUAL	(Expression.getConnective(RCLexer.GREATERTHANOREQUAL)),
		NOTEQUAL  			(Expression.getConnective(RCLexer.NOTEQUAL));

		private final String connective;

		private Type ( String connective ) {
			this.connective = connective;
		}

		public String getConnective() {
			return connective;
		}
	}

	private final Type type;

	public Condition (Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
}
