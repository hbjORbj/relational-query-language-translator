package honours_project;

public class Comparison {
	public static enum ComparisonOperator {
		LT {
			public <Element extends Comparable<Element>> boolean compare(Element e1, Element e2) {
				return (e1.compareTo(e2) < 0);
			};
		},
		LE {
			public <Element extends Comparable<Element>> boolean compare(Element e1, Element e2) {
				return (e1.compareTo(e2) <= 0);
			};
		},
		EQ {
			public <Element extends Comparable<Element>> boolean compare(Element e1,Element e2) {
				return (e1.compareTo(e2) == 0);
			};
		},
		NE {
			public <Element extends Comparable<Element>> boolean compare(Element e1,Element e2) {
				return (e1.compareTo(e2) != 0);
			};
		},
		GE {
			public <Element extends Comparable<Element>> boolean compare(Element e1,Element e2) {
				return (e1.compareTo(e2) >= 0);
			};
		},
		GT {
			public <Element extends Comparable<Element>> boolean compare(Element e1,Element e2) {
				return (e1.compareTo(e2) > 0);
			};
		};
		public abstract <Element extends Comparable<Element>> boolean compare(Element e1, Element e2);
	}
}
