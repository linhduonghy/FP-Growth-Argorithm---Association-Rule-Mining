package fp_growth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Test {
	
	public static void main(String[] args) throws IOException {
		String dataPath = "data/association_rules.csv";
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
				if (i == 6) {
					s = s.substring(1);
				}
				String[] ss = s.split("=");
				l.add(new Item(ss[0].trim()));
			}
			r.add(new Item(x.get(x.size() - 1).split("=")[0].trim()));
			Double conf = Double.parseDouble(x.get(1));
			AssociationRule rule = new AssociationRule();
			rule.setLeft_side(l);
			rule.setRight_side(r);
			rule.setConf(conf);
			rules.add(rule);
		});
		rules.forEach(rule -> {
			System.out.println(rule);
		});
		
		
//		Set<String> unique_items = new HashSet<String>();
//
//		records.forEach(record -> {
//			record.forEach(item -> {
//				unique_items.add(item);
//			});
//			
//		});
//		
//		List<List<String>> exists = new ArrayList<List<String>>();
//		exists.add(new ArrayList<String>(unique_items));
//				
//		records.forEach(record -> {
//			List<String> temp = new ArrayList<>();
//			exists.get(0).forEach(item -> {
//				if (record.contains(item)) {
//					temp.add("1");
//				} else {
//					temp.add("");
//				}
//			});
//			exists.add(temp);
//		});
//		
//		List<String> res = new ArrayList<String>();
//		for (List<String> l : exists) {
//			String t = "";
//			for (String i : l) {
//				t += i + ";";
//			}
//			res.add(t.substring(0, t.length() - 1));
//		}
//		
//		BufferedWriter out = new BufferedWriter(new FileWriter("data/test.csv"));
//		for (String s : res) {
//			out.write(s);
//			out.write("\n");
//		}
//		out.close();
	}
	
}
