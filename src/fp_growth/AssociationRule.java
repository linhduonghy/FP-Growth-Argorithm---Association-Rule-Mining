package fp_growth;

import java.util.ArrayList;
import java.util.List;

public class AssociationRule{
	
	private List<Item> left_side;
	private List<Item> right_side;
	private double conf;
	
	public AssociationRule() {
		// TODO Auto-generated constructor stub
	}
	
	public AssociationRule(List<Item> left_side, List<Item> right_side) {
		super();
		this.left_side = left_side;
		this.right_side = right_side;
	}

	public List<Item> getLeft_side() {
		return left_side;
	}

	public void setLeft_side(List<Item> left_side) {
		this.left_side = left_side;
	}

	public List<Item> getRight_side() {
		return right_side;
	}

	public void setRight_side(List<Item> right_side) {
		this.right_side = right_side;
	}

	public double getConf() {
		return conf;
	}

	public void setConf(double conf) {
		this.conf = conf;
	}

	@Override
	public String toString() {
		return "" + left_side + "->" + right_side + ":" + conf /*(double) Math.round(conf * 1000) / 1000*/ +  "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left_side == null) ? 0 : left_side.hashCode());
		result = prime * result + ((right_side == null) ? 0 : right_side.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationRule other = (AssociationRule) obj;
		List<Item> l1 = new ArrayList<Item>(left_side);
		List<Item> l2 = new ArrayList<Item>(other.getLeft_side());
		List<Item> r1 = new ArrayList<Item>(right_side);
		List<Item> r2 = new ArrayList<Item>(other.right_side);
		return l1.equals(l2) && r1.equals(r2);
	}

	
}
