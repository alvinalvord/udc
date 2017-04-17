public class SecretaryView extends ClinicView {
	
	public SecretaryView (ClinicViewController cvc) {
		super (cvc);
		setViewLabel ("Secretary's View");
		agendaDayScroller.setSecretaryMode (true);
		calendarDayScroller.setSecretaryMode (true);
		calendarWeekScroller.setSecretaryMode (true);
		agendaWeekScroller.setSecretaryMode (true);
	}
	
}