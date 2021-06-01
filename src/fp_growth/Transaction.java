package fp_growth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transaction {

	private List<Item> items;
	
	public Transaction(List<Item> items) {
		super();
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		Transaction other = (Transaction) obj;
		List<Item> i1 = new ArrayList<Item>(items);
		List<Item> i2 = new ArrayList<Item>(other.items);
		Collections.sort(i1);
		Collections.sort(i2);
		return i1.equals(i2);
	}

	@Override
	public String toString() {
		return "" + this.items;
	}
}
