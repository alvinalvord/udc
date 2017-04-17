public class SecretaryView extends ClinicView {
	
	public SecretaryView (DatabaseControl dc) {
		super (dc);
		setViewLabel ("Secretary's View");
		
		leftVbox.getChildren ().remove (create);
		
		agendaDayScroller.setSecretaryMode (true);
		calendarDayScroller.setSecretaryMode (true);
		calendarWeekScroller.setSecretaryMode (true);
		agendaWeekScroller.setSecretaryMode (true);
	}
	
}