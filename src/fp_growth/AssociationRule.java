package fp_growth;

import java.util.List;

public class AssociationRule {
	
	private List<Item> left_side;
	private List<Item> right_side;
	private float conf;
	
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

	public float getConf() {
		return conf;
	}

	public void setConf(float conf) {
		this.conf = conf;
	}

	@Override
	public String toString() {
		return "" + left_side + "->" + right_side + ":" + (float) Math.round(conf * 100) / 100 + "\n";
	}
	
	
}
