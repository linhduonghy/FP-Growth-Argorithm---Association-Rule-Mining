package fp_growth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class HeaderTable {
	
	private Map<Item, LinkedList<FPNode>> headerTable;

	public HeaderTable() {
		headerTable = new HashMap<Item, LinkedList<FPNode>>();
	}
	public Map<Item, LinkedList<FPNode>> getHeaderTable() {
		return headerTable;
	}

	public void setHeaderTable(Map<Item, LinkedList<FPNode>> headerTable) {
		this.headerTable = headerTable;
	}
	
	public void createHeader(Item item) {
		this.headerTable.put(item, null);
		
	}
	public void addHeader(Item item, FPNode node) {
		LinkedList<FPNode> link = this.headerTable.get(item);
		if (link == null) {
			link = new LinkedList<FPNode>();
		}
		link.add(node);
		this.headerTable.put(item, link);
	}
	@Override
	public String toString() {
		return ""+this.headerTable;
	}
}	
