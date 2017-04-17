public class SecretaryView extends ClinicView {
	
	public SecretaryView (DatabaseControl dc) {
		super (dc);
		setViewLabel ("Secretary's View");
		agendaDayScroller.setSecretaryMode (true);
		calendarDayScroller.setSecretaryMode (true);
		calendarWeekScroller.setSecretaryMode (true);
		agendaWeekScroller.setSecretaryMode (true);
	}
	
}