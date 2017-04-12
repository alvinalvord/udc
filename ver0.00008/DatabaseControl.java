public interface DatabaseControl {
	public void deleteRow (int id);
	public void markDone (int id);
	public void createUpdate (int id, String... param);
	public int createInsert (String... param);
	public int getAccessLevel (int id);
	public java.sql.ResultSet getUpdates (String id, int y, int m, int d);
}