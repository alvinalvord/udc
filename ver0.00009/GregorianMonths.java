public enum GregorianMonths implements Months {
	January				(0),
	February				(1),
	March					(2),
	April					(3),
	May						(4),
	June					(5),
	July						(6),
	August					(7),
	September			(8),
	October				(9),
	November			(10),
	December			(11);
	
	private final int month;
	
	GregorianMonths (int month) {
		this.month = month;
	}
	
	public int getMonth () {
		return month;
	}
	
	public Months selectMonth (int n) {
		switch (n) {
			case 0:
				return January;
				
			case 1:
				return February;
				
			case 2:
				return March;
				
			case 3:
				return April;
				
			case 4:
				return May;
				
			case 5:
				return June;
				
			case 6:
				return July;
				
			case 7:
				return August;
				
			case 8:
				return September;
				
			case 9:
				return October;
				
			case 10:
				return November;
				
			case 11:
				return December;
		}
		
		return January;
	}
	
}