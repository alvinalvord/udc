public interface DatabaseControl {
	public void deleteRow (int id);
	public void createUpdate (int id, String... param);
	public int createInsert (String... param);
	public int getAccessLevel (int id);
	public ClinicView getView ();
	public java.sql.ResultSet getDisplayData (int y, int m, int d);
}