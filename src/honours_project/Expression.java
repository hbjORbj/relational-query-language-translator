package honours_project;

import honours_project.Schema; // Doesn't exist yet
import honours_project.SchemaException; // Doesn't exist yet
import honours_project.parsing.RCLexer; // Doesn't exist yet
import honours_project.parsing.RCParser; // Doesn't exist yet
import honours_project.parsing.RealListener; // Doesn't exist yet

public abstract class Expression {
	public static Expression parse(String s) throws RecognitionException, ReplacementException {
		RCLexer lexer = new RCLexer(CharStreams.fromString(s));
		lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		RCParser parser = new RCParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		parser.addErrorListener(ThrowingErrorListener.INSTANCE);
		try {
			// ParseTree tree = parser.start(); ANTLR
			RealListener listener = new RealListener();
			// ParseTreeWalker.DEFAULT.walk(listener, tree); ANTLR
			return listener.parsedExpression();
		} catch (RuntimeException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RecognitionException) {
				throw (RecognitionException) cause;
			} else if (cause instanceof ReplacementException) {
				throw new ReplacementException(e.getMessage(), cause);
			} else {
				throw e;
			}
		}
	}
	
	private static String getConnective(int tokenID) {
		String literal = RCLexer.VOCABULARY.getLiteralName(tokenID);
		return literal.replaceAll("", "");
	}
	
	public static enum Type {
		ATOMIC (Expression.getConnective(RCLexer.ATOMIC)),
		NEGATION (Expression.getConnective(RCLexer.NEGATION)),
		CONJUNCTION (Expression.getConnective(RCLexer.CONJUNCTION)),
		DISJUNCTION (Expression.getConnective(RCLexer.DISJUNCTION)),
		IMPLICATION (Expression.getConnective(RCLexer.IMPLICATION)),
		COMPARISON (Expression.getConnective(RCLexer.COMPARISON)),
		UNIVERSAL (Expression.getConnective(RCLexer.UNIVERSAL)),
		EXISTENTIAL (Expression.getConnective(RCLexer.EXISTENTIAL))
		
		private final String connective;
		
		private Type (String connective) {
			this.connective = connective;
		}
		
		public String getConnective() {
			return connective;
		}
	}

}
