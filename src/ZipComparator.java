import java.util.Comparator;

public class ZipComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public ZipComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String zip1 = c1.getZip();
		String zip2 = c2.getZip();
		
		if(sortOrder == SortOrder.ascending)return zip1.compareTo(zip2);
		else return zip2.compareTo(zip1);
	}
}

