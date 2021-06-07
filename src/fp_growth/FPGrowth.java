package fp_growth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FPGrowth {

	private static String dataPath = "data/groceries2.csv";
	private static double min_sup = (double) 0.001; // adjust min_support
	private static double min_conf = (double) 0.9; // adjust min_confident

	
	public static void main(String[] args) {
		
		System.err.println("\n min_sup = " + min_sup + ", min_conf = " + min_conf);
		List<List<String>> records = loadData(dataPath);				
		
		long startTime = System.currentTimeMillis();
		
		min_sup = min_sup * records.size();
				
		Map<Transaction, Integer> transactions = createTransactions(records, min_sup);
		
//		 build tree
		FPTree fpTree = new FPTree(transactions, min_sup);
//		System.err.println("Frequent 1-Itemset: " + fpTree.getFrequent().size()+"\n");
//		fpTree.getFrequent().keySet().forEach(k -> System.out.println(k + " " + fpTree.getFrequent().get(k)));

//		System.err.println("FP Tree\n");
//		System.out.println(fpTree.getRoot());

//		mine tree find frequent patterns
		Map<List<Item>, Integer> frequent_patterns = new HashMap<List<Item>, Integer>();
		mine_pattern(fpTree, new ArrayList<Item>(), frequent_patterns, min_sup);

		System.err.println("\n-----------Frequent Itemsets-----------\n");
		System.err.println(frequent_patterns.size() + " itemsets\n");
//		frequent_patterns.forEach((items, count) -> System.out.println(items + " " + count));

		Set<AssociationRule> association_rules = new HashSet<AssociationRule>();
		gen_association_rules(frequent_patterns, association_rules, min_conf);
		System.err.println("\n-----------Association Rules-----------\n");

		List<AssociationRule> sortedArs = sortAssociationRuleByConf(association_rules);
//		System.err.println(sortedArs.size() + " rules generated\n");
//		for (AssociationRule ar : sortedArs) {
//			System.out.println(ar);
//		}
		System.err.println(sortedArs.size() + " rules generated\n");
		long endTime = System.currentTimeMillis();
		System.err.print("Total time: " + (double)(endTime - startTime) / 1000 + "s\n");
		
		List<AssociationRule> testRules = createTestRules();
		System.out.println("\nCheck rules generated with rules Orange tool generated : " + checkRules(sortedArs, testRules));
	}
	
	private static boolean checkRules(List<AssociationRule> rules1 , List<AssociationRule> rules2) {
		for (AssociationRule r2 : rules2) {
			if (!rules1.contains(r2)) {
//				System.out.println(r2);
				return false;
			}
		}
		return true;
	}
	private static List<AssociationRule> createTestRules() {
		String dataPath = "data/orange_rules/0_001_0_9.csv";
		List<List<String>> records = new ArrayList<List<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(dataPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				records.add(Arrays.asList(values));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<AssociationRule> rules = new ArrayList<AssociationRule>();
		records.forEach(x -> {
			List<Item > l = new ArrayList<>();
			List<Item > r = new ArrayList<>();
			
			for (int i = 6; i < x.size() - 1; ++i) {
				String s = x.get(i);
				if (i == 6 && s.startsWith("\"")) {
					s = s.substring(1);			
				}
				if (s.startsWith("\"")) {
					
					s = s.substring(1);
					
				}
				String[] ss = s.split("=");
				l.add(new Item(ss[0].trim()));
			}
			r.add(new Item(x.get(x.size() - 1).split("=")[0].trim()));
			Double conf = Double.parseDouble(x.get(1));
			AssociationRule rule = new AssociationRule();
			
			Collections.sort(l);
			Collections.sort(r);
			rule.setLeft_side(l);
			rule.setRight_side(r);
			rule.setConf(conf);
			rules.add(rule);
		});
		return rules;
	}

	private static List<List<String>> loadData(String dataPath) {
		
		List<List<String>> records = new ArrayList<List<String>>();

		try (BufferedReader br = new BufferedReader(new FileReader(dataPath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				records.add(Arrays.asList(values));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}

	private static List<AssociationRule> sortAssociationRuleByConf(Set<AssociationRule> association_rules) {
		
		List<AssociationRule> ars = new ArrayList<AssociationRule>(association_rules);
		Collections.sort(ars, (a1, a2) -> {
			if (a2.getConf() < a1.getConf()) 
				return -1;
			if (a2.getConf() > a1.getConf()) 
				return 1;
			return 0;
		});
		
		return ars;
	}

	private static Map<Transaction, Integer> createTransactions(List<List<String>> data, double min_sup) {
		Map<Transaction, Integer> transactions = new HashMap<Transaction, Integer>();
		data.forEach(trans -> {
			List<Item> items = new ArrayList<Item>();
			trans.forEach(x -> {
				Item item = new Item(x);
				items.add(item);
			});
			Collections.sort(items, (i1, i2) -> {
				return i1.getName().compareTo(i2.getName());
			});
			Transaction t = new Transaction(items);
			Integer transCount = transactions.get(t) == null ? 0 : transactions.get(t);
			transactions.put(t, transCount + 1);
		});
		return transactions;
	}

	public static void mine_pattern(FPTree fpTree, List<Item> prefix, Map<List<Item>, Integer> frequent_patterns,
			double min_sup) {

		List<Map.Entry<Item, Integer>> frequent_items = new ArrayList<Map.Entry<Item, Integer>>(
				fpTree.getFrequent().entrySet());
		Collections.sort(frequent_items, (o1, o2) -> {
			return o1.getValue().compareTo(o2.getValue());
		});

//		System.out.println(frequent_items);

		for (Map.Entry<Item, Integer> entry : frequent_items) {
			Item baseItem = entry.getKey();
			Integer count_base_item = entry.getValue();

			// get count of prefix pattern
			// if prefix is not exist
			// set count prefix = count base item
			Integer count_prefix = frequent_patterns.get(prefix);
			if (count_prefix == null) {
				count_prefix = count_base_item;
			}
			List<Item> newFrequentSet = new ArrayList<Item>(prefix);
			// add new item to prefix pattern
			newFrequentSet.add(baseItem);
			// get count of new frequent pattern
			// if pattern is not exist
			// set count = min(count_base_item, count_prefix)
			Integer count_new_pattern = frequent_patterns.get(newFrequentSet);
			if (count_new_pattern == null) {
				count_new_pattern = Math.min(count_base_item, count_prefix);
			} else {
				count_new_pattern += Math.min(count_base_item, count_prefix);
			}
			frequent_patterns.put(newFrequentSet, count_new_pattern);

//			System.err.println("-----------------\nBase Item: " + baseItem + "(" + count_base_item + ")");
//			System.out.println("new set: " + newFrequentSet);
			Map<Transaction, Integer> condPatternBase = fpTree
					.findConditionalPatternBase(fpTree.getHeaders().getHeaderTable().get(baseItem));
//			System.out.println("cond pattern base: " + condPatternBase);

			FPTree sub_fptree = new FPTree(condPatternBase, min_sup);

			if (sub_fptree.getHeaders() != null) {
				mine_pattern(sub_fptree, newFrequentSet, frequent_patterns, min_sup);
			}
		}
	}

	private static void gen_association_rules(Map<List<Item>, Integer> frequent_patterns,
			Set<AssociationRule> association_rules, double min_conf) {

		Map<List<Item>, Integer> patterns = new HashMap<List<Item>, Integer>();
		// sort list item in pattern by item name
		Iterator<Map.Entry<List<Item>, Integer>> it = frequent_patterns.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<List<Item>, Integer> entry = it.next();
			List<Item> items = new ArrayList<Item>(entry.getKey());
			Collections.sort(items);
			patterns.put(items, entry.getValue());
			it.remove();
		}

		for (Map.Entry<List<Item>, Integer> entry : patterns.entrySet()) {

			List<Item> frequent_itemsets = entry.getKey();

			List<Item> left_side = new ArrayList<Item>(frequent_itemsets);

			if (left_side.size() == 1) {
				continue;
			}

			List<Item> right_side = new ArrayList<Item>();

			Queue<AssociationRule> queue = new LinkedList<AssociationRule>();
			queue.add(new AssociationRule(left_side, right_side));
			
			while (!queue.isEmpty()) {
				AssociationRule ar = queue.poll();
				double conf = computeConfident(ar, patterns);
				if (conf >= min_conf) {
					if (ar.getRight_side().size() > 0) {
						ar.setConf(conf);
						association_rules.add(ar);							
					}
					Set<AssociationRule> ars = gen_candidate_rule(ar);
					if (ars != null)
						queue.addAll(ars);
				}
			}
		}
	}

	private static double computeConfident(AssociationRule ar, Map<List<Item>, Integer> frequent_patterns) {
		List<Item> itemset = new ArrayList<Item>(ar.getLeft_side());
		itemset.addAll(ar.getRight_side());
		Collections.sort(itemset);
		Collections.sort(ar.getLeft_side());
		return (double) (frequent_patterns.get(itemset)) / frequent_patterns.get(ar.getLeft_side());
	}

//	gen X -> Y - X
	private static Set<AssociationRule> gen_candidate_rule(AssociationRule a_rule) {

		if (a_rule.getLeft_side().size() == 1) {
			return null;
		}
		Set<AssociationRule> candidate_rules = new HashSet<>();

		// remove one item in left side and add to right side
		for (Item item : a_rule.getLeft_side()) {
			List<Item> new_left_side = new ArrayList<Item>(a_rule.getLeft_side());
			List<Item> new_right_side = new ArrayList<Item>(a_rule.getRight_side());
			new_left_side.remove(item);
			new_right_side.add(item);
			Collections.sort(new_left_side);
			Collections.sort(new_right_side);
			candidate_rules.add(new AssociationRule(new_left_side, new_right_side));
		}

		return candidate_rules;
	}

}
//[[1, 2, 5],
//[2, 4],
//[2, 3],
//[1, 2, 4],
//[1, 3],
//[2, 3],
//[1, 3],
//[1, 2, 3, 5],
//[1, 2, 3]]
