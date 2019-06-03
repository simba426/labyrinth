package qlearning;
import java.util.Arrays;

public class State {
	private int[] stateData;

	public State(int... stateData) {
		this.stateData = stateData;
	}

	public int[] getStateData() {
		return stateData;
	}

	@Override
	public String toString() {
		return Arrays.toString(stateData);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof State)) {
			return false;
		}
		State o = (State) other;
		return Arrays.equals(stateData, o.stateData);
	}
}
