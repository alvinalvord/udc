import java.util.*;

public abstract class Model {
	
	protected ArrayList <View> views = new ArrayList <View> ();
	
	public void attach (View v) {
		views.add (v);
	}
	
	public void detach (View v) {
		views.remove (v);
	}
	
	public void notifyViews () {
		for (View v: views)
			v.update ();
	}
	
}