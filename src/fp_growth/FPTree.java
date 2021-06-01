package fp_growth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FPTree {
	
	private FPNode root;
	private HeaderTable headers;
	private Map<Item, Integer> frequent;
	
	public FPNode getRoot() {
		return root;
	}
	public HeaderTable getHeaders() {
		return headers;
	}
	public Map<Item, Integer> getFrequent() {
		return frequent;
	}
	
	public FPTree(Map<Transaction, Integer> transactions, int min_sup) {
		this.frequent = find_frequent_items(transactions, min_sup);
		this.headers = build_header_table(frequent);
		this.root = build_fptree(transactions, frequent, headers, min_sup);
//		System.out.println(root);
	}

	private Map<Item, Integer> find_frequent_items(Map<Transaction, Integer> transactions, int min_sup) {
		Map<Item, Integer> frequent_items = new HashMap<Item, Integer>();
		// count frequent item
		for (Map.Entry<Transaction, Integer> entry : transactions.entrySet()) {
			Transaction trans = entry.getKey();
			Integer countTrans = entry.getValue();
			for (Item item : trans.getItems()) {
				Integer cnt = frequent_items.get(item) != null ? frequent_items.get(item) : 0;
				frequent_items.put(item, cnt + countTrans);
			}
		}
		
		// remove item with frequency < min_sup
		Iterator<Map.Entry<Item, Integer>> it = frequent_items.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Item, Integer> entry = it.next();
			if (entry.getValue() < min_sup) {
				it.remove();
			}
		}
//		System.out.println(frequent_items);
		return frequent_items;
	}

	private HeaderTable build_header_table(Map<Item, Integer> frequent) {
		if (frequent == null || frequent.isEmpty())
			return null;
		HeaderTable headers = new HeaderTable();
		for (Item item : frequent.keySet()) {
			headers.createHeader(item);
		}
		return headers;
	}

	private FPNode build_fptree(Map<Transaction, Integer> transactions,
			Map<Item, Integer> frequent, HeaderTable headers, Integer min_sup) {

		// initialize root node with null
		FPNode root = new FPNode(null, null, null);
		// remove infrequent in each transaction
		for (Map.Entry<Transaction, Integer> trans : transactions.entrySet()) {		
			Iterator<Item> it = trans.getKey().getItems().iterator();
			while (it.hasNext()) {
				Item item = it.next();
				if (frequent.get(item) == null) {
					it.remove();
				}
			}
		}
		// loop transactions and add to tree
		for (Map.Entry<Transaction, Integer> trans : transactions.entrySet()) {
			// sort list item in the transaction by frequent item
			trans.getKey().getItems().sort((i1, i2) -> {
				Integer c1 = frequent.get(i1) == null ? 0 : frequent.get(i1);
				Integer c2 = frequent.get(i2) == null ? 0 : frequent.get(i2);
				return c2 - c1;
			});
			// insert transaction to tree
			insert_tree(trans, root, headers);
		}
		return root;
	}

	/** 
	 * loop item in transaction and add to tree
	 * */
	private void insert_tree(Map.Entry<Transaction, Integer> trans, FPNode root, HeaderTable headers) {
		
		Transaction t = trans.getKey();
		Integer cnt = trans.getValue();
		
		FPNode current_node = root;
		
		for (Item item : t.getItems()) {
			FPNode child_node = current_node.getChild(item);
			// if current node has child with item value
			// increase count child
			if (child_node != null) {
				child_node.increaseCount(cnt);
			} 
			// current node hasn't child with item value
			// add child to current node
			// add child to header table
			else {
				child_node = current_node.add_child(item, cnt);
				// link child node to header table
				headers.addHeader(item, child_node);
			}
			// continue with child node
			current_node = child_node;
		}
		
	}
	
	public Map<Transaction, Integer> findConditionalPatternBase(LinkedList<FPNode> header) {
		
		Map<Transaction, Integer> condP = new HashMap<Transaction, Integer>();
		
		for (FPNode node : header) {
			List<Item> prefixPath = findPrefixPath(node);
			if (prefixPath == null) {
				continue;
			}
			Transaction t = new Transaction(prefixPath);
			Integer count = condP.get(t) == null ? 0 : condP.get(t);
			condP.put(t, count + node.getCount());
		}
		
		return condP;
	}
	
	private List<Item> findPrefixPath(FPNode node) {
		node = node.getParent();
		List<Item> prefixPath = new ArrayList<Item>();
		while (node.getParent() != null) {
			prefixPath.add(node.getValue());
			node = node.getParent();
		}
		return prefixPath.size() == 0 ? null : prefixPath;
	}
	
	
}
