package s1705270;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import s1705270.RCBaseListener;
import s1705270.RCParser;

public class RealListener extends RCBaseListener {
	
	private Stack<Formula> formulaStack = new Stack<Formula>();
	private List<Term> varList = null;
	private Stack<List<Term>> varListStack = new Stack<List<Term>>();
	
	private Stack<Term> termStack = new Stack<Term>();
	private Stack<Comparison> comparisonStack = new Stack<Comparison>();
	private Stack<Term> varStack = new Stack<Term>();
	
	private Stack<String> nameStack = new Stack<String>(); // For Predicate names

	private Formula parsedFormula = null;

	public Formula parsedFormula() {
		if (formulaStack.size() != 1) {
			// something went wrong
		}
		if (parsedFormula == null) {
			parsedFormula = formulaStack.pop();
		}
		return parsedFormula;
	}
	
	@Override public void enterVariable(RCParser.VariableContext ctx) { }

	@Override public void exitVariable(RCParser.VariableContext ctx) { }

	@Override public void enterVariable_list(RCParser.Variable_listContext ctx) {
		varList = new ArrayList<Term>();
	}

	@Override public void exitVariable_list(RCParser.Variable_listContext ctx) {
		varListStack.add(varList);
	}

	@Override public void enterConstant(RCParser.ConstantContext ctx) { }

	@Override public void exitConstant(RCParser.ConstantContext ctx) {
		String value = ctx.getText().replace("'", "");
		termStack.push(new Term(value, true));
	}

	@Override public void enterTerm(RCParser.TermContext ctx) { }

	@Override public void exitTerm(RCParser.TermContext ctx) { }

	@Override public void enterPredicate(RCParser.PredicateContext ctx) { }

	@Override public void exitPredicate(RCParser.PredicateContext ctx) {
		String name = nameStack.pop();
		List<Term> terms = varListStack.pop();
		formulaStack.push(new Predicate(name, terms));
	}
	
	@Override public void enterDisjunction(RCParser.DisjunctionContext ctx) {
	}

	@Override public void exitDisjunction(RCParser.DisjunctionContext ctx) {
		Formula right = formulaStack.pop();
		Formula left = formulaStack.pop();
		formulaStack.push(new Disjunction(left, right));
	}
	
	@Override public void enterConjunction(RCParser.ConjunctionContext ctx) { }

	@Override public void exitConjunction(RCParser.ConjunctionContext ctx) {
		Formula right = formulaStack.pop();
		Formula left = formulaStack.pop();
		formulaStack.push(new Conjunction(left, right));
	}
	
	@Override public void enterImplication(RCParser.ImplicationContext ctx) { }

	@Override public void exitImplication(RCParser.ImplicationContext ctx) {
		Formula right = formulaStack.pop();
		Formula left = formulaStack.pop();
		formulaStack.push(new Implication(left, right));
	}
	
	@Override public void enterNegation(RCParser.NegationContext ctx) { }

	@Override public void exitNegation(RCParser.NegationContext ctx) {
		Formula f = formulaStack.pop();
		formulaStack.push(new Negation(f));
	}

	@Override public void enterUniversalQuantifier(RCParser.UniversalQuantifierContext ctx) { }

	@Override public void exitUniversalQuantifier(RCParser.UniversalQuantifierContext ctx) {
		Formula formula = formulaStack.pop();
		List<Term> terms = varListStack.pop();
		formulaStack.push(new Universal(terms, formula));
	}

	@Override public void enterExistentialQuantifier(RCParser.ExistentialQuantifierContext ctx) { }

	@Override public void exitExistentialQuantifier(RCParser.ExistentialQuantifierContext ctx) {
		Formula formula = formulaStack.pop();
		List<Term> terms = varListStack.pop();
		formulaStack.push(new Existential(terms, formula));
	}

	@Override public void enterEquality(RCParser.EqualityContext ctx) { }

	@Override public void exitEquality(RCParser.EqualityContext ctx) {
		Term right = termStack.pop();
		Term left = termStack.pop();
		comparisonStack.push(new Equality(left, right));
	}
	
	@Override public void enterLessThan(RCParser.LessThanContext ctx) { }

	@Override public void exitLessThan(RCParser.LessThanContext ctx) {
		Term right = termStack.pop();
		Term left = termStack.pop();
		comparisonStack.push(new LessThan(left, right));
	}

}
