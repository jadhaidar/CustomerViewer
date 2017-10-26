import java.util.Comparator;

public class StateComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public StateComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String state1 = c1.getState();
		String state2 = c2.getState();
		
		if(sortOrder == SortOrder.ascending)return state1.compareTo(state2);
		else return state2.compareTo(state1);
	}
}

