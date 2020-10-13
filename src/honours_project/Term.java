package honours_project;

public interface Term<Relation, Element> {

	public static class Constant<Relation, Element> implements Term<Relation, Element> {
		public final Relation relation;
		public final Element value;

		private Constant(Relation relation, Element value) {
			super();
			this.relation = relation;
			this.value = value;
		}
		
		public static <Relation, Element> Constant<Relation, Element> getConstant(Relation relation, Element value) {
			return new Constant<Relation, Element>(relation, value);
		}
		
		public Relation getRelation() {
			return this.relation;
		}
		
		public Element getValue() {
			return this.value;
		}
	}
	
	public enum Precedence implements Comparable<Precedence> {
		ATOMIC, COMPARISON, NEGATION, CONJUNCTION, DISJUNCTION, IMPLICATION, UNIVERSAL, EXISTENTIAL;
	}
}
