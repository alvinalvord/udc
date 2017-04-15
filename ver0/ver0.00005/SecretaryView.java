public class SecretaryView extends ClinicView {
	
	public SecretaryView (ClinicViewController cvc) {
		super (cvc);
		setViewLabel ("Secretary's View");
		agendaDayScroller.setSecretaryMode (true);
	}
	
}