import java.util.Comparator;

public class WebComparator implements Comparator<Customer> {
	SortOrder sortOrder;
	
	public WebComparator(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public int compare(Customer c1, Customer c2) {
		String web1 = c1.getWeb();
		String web2 = c2.getWeb();
		
		if(sortOrder == SortOrder.ascending)return web1.compareTo(web2);
		else return web2.compareTo(web1);
	}
}