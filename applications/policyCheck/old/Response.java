import java.util.ArrayList;

import verify.example.types.State;


public class Response {
	boolean granted;
	State state;
	/**
	 * @param args
	 */
	
	public boolean getGranted() {
		return granted;
	}
	public State getState() {
		return state;
	}
	 Response(boolean granted, State state) {
		this.granted = granted;
		this.state = state;
	}

}
