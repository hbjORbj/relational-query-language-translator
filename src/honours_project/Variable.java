package honours_project;

public class Variable<Relation, Element> implements Term<Relation, Element> {
	private Relation relation;
	private Element name;
	
	public Variable(Relation relation, Element name) {
		if (relation == null) {
			throw new IllegalArgumentException("Relation is null.");
		}
		this.relation = relation;
		this.name = name;
	}
	
	public Relation getRelation() {
		return this.relation;
	}
	
	public Element getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object object) {
		return (this == object) || ((object instanceof Variable<?,?>) && equals((Variable<?,?>) object));
	}
	
	private boolean equals(Variable<?,?> other) {
		return this.relation.equals(other.getRelation()) && this.name.equals(other.getName());
	}
}
