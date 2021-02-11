package s1705270;

import java.util.Set;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import uk.ac.ed.pguaglia.real.parsing.RALexer;

public abstract class Formula {
	
	public static Formula parse(String s) throws RecognitionException {
		RCLexer lexer = new RCLexer(CharStreams.fromString(s));
		//lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
		RCParser parser = new RCParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		//parser.addErrorListener(ThrowingErrorListener.INSTANCE);
		try {
			ParseTree tree = parser.formula();
			RCListenerImpl listener = new RCListenerImpl();
			ParseTreeWalker.DEFAULT.walk(listener, tree);
			return listener.parsedFormula();
		} catch (RuntimeException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RecognitionException) {
				throw (RecognitionException) cause;
			} else {
				throw e;
			}
		}
	}

	private static String getConnective(int tokenID) {
		String literal = RCLexer.VOCABULARY.getLiteralName(tokenID);
		return literal.replaceAll("'", "");
	}

	public static enum Type {
		PREDICATE(""),
		COMPARISON(""),
		NEGATION(Formula.getConnective(RCLexer.NEGATION)),
		CONJUNCTION(Formula.getConnective(RCLexer.CONJUNCTION)),
		DISJUNCTION(Formula.getConnective(RCLexer.DISJUNCTION)),
		IMPLICATION(Formula.getConnective(RCLexer.IMPLICATION)),
		UNIVERSAL(Formula.getConnective(RCLexer.UNIVERSAL)),
		EXISTENTIAL(Formula.getConnective(RCLexer.EXISTENTIAL));

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
