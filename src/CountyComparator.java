import java.util.Comparator;

public class CountyComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public CountyComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String county1 = c1.getCounty();
		String county2 = c2.getCounty();
		
		if(sortOrder == SortOrder.ascending)return county1.compareTo(county2);
		else return county2.compareTo(county1);
	}
}

