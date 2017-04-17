public enum GregorianDays implements Days {
	Sunday			(0, "Sun"),
	Monday		(1, "Mon"),
	Tuesday		(2, "Tue"),
	Wednesday	(3, "Wed"),
	Thursday		(4, "Thu"),
	Friday			(5, "Fri"),
	Saturday		(6, "Sat");
	
	private final int day;
	private final String header;
	
	GregorianDays (int day, String header) {
		this.day = day;
		this.header = header;
	}
	
	public int getDay () {
		return day;
	}
	
	public String getHeader () {
		return header;
	}
	
}