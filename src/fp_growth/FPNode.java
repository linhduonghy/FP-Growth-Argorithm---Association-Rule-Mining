package fp_growth;

import java.util.ArrayList;
import java.util.List;

public class FPNode {
	
	/**
	 * A node in the FP Tree
	 * 
	 * */
	
	private Item value;
	private Integer count;
	private FPNode parent;
	private List<FPNode> children;
	
	public FPNode(Item value, Integer count, FPNode parent) {
		this.value = value;
		this.count = count;
		this.parent = parent;
		this.children = new ArrayList<FPNode>();
	}

	public Item getValue() {
		return value;
	}

	public void setValue(Item value) {
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public FPNode getParent() {
		return parent;
	}

	public void setParent(FPNode parent) {
		this.parent = parent;
	}

	public List<FPNode> getChildren() {
		return children;
	}

	public void setChildren(List<FPNode> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return display(0);
	}
	// get children with item
	public FPNode getChild(Item item) {
		for (FPNode child : this.children) {
			if (item.equals(child.getValue())) {
				return child;
			}
		}
		return null;
	}
	public String display(int level) {
		String ret = "";
		if (level == 0) {
			System.out.println("FP Tree: ");
		}
		for (int i = 0; i < level; ++i) 
			ret += "\t";
 
		ret += this.value;
		ret += this.count == null ? "" : "(" + this.count + ")";
		ret += "\n";
		
		for (FPNode child : this.children) {
			ret += child.display(level + 1);
		}
		return ret;
	}

	public void increaseCount(Integer cnt) {
		this.count += cnt;
	}
	
	public FPNode add_child(Item item, Integer count) {
		FPNode node = new FPNode(item, count, this);
		this.children.add(node);
		return node;
	}
}
