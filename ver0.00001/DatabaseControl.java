public interface DatabaseControl {
	public void deleteRow (int id);
	public void createUpdate (int id, String... param);
	public int createInsert (String... param);
}