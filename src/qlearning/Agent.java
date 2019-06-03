package qlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Agent {
	public static Map<String, Double> memory;
	protected State lastState;
	protected Action lastAction;
	protected double alpha;
	protected double gamma;
	protected double epsilon;

	public Agent() {
		memory = new HashMap<>();
		alpha = 1;
		gamma = 1;
		epsilon = 0.01;
	}

	public Action nextAction(State currentState, double rewardForGettingToThisState) {
		Action[] actions = Action.values();
		// Find the best action for 'currentState'
		double maxScore = Integer.MIN_VALUE;
		List<Action> bestActions = new ArrayList<>();
                //try all the action for current state, find the best actions
		for (int i = 0; i < actions.length; i++) {
			Action potentialAction = actions[i];
			String entry = currentState.toString() + potentialAction.toString(); //what is the state?
			if (memory.get(entry) == null) {//if the state don't exist, we should put this state into the memory
				memory.put(entry, getInitialValue());
			}
			double score = memory.get(entry);
			if (score > maxScore) {
				maxScore = score;
				bestActions.clear();
				bestActions.add(potentialAction);
			} else if (score == maxScore) {
				bestActions.add(potentialAction);
				// If more than one actions have the same maximum score then add then to a list
				// and pick and action from that list at random
			}
		}
                //choose one of the best action or the random action
		Action finalAction;
		Action bestAction = bestActions.get((int) (Math.random() * bestActions.size()));
		// Epsilon greedy method. There's a chance a random action will be picked.
		if (Math.random() < epsilon) {
			finalAction = actions[(int) (Math.random() * actions.length)];
		} else {
			finalAction = bestAction;
		}
		
                //for the best action , just put it and its state to the memory
		if (lastState != null && lastAction != null) {
			// Update the value of the last state-action pair
			double lastEntryScore = Q(lastState, lastAction) + alpha
					* (rewardForGettingToThisState + gamma * Q(currentState, bestAction) - Q(lastState, lastAction));
			storeQ(lastState, lastAction, lastEntryScore);
		}

		lastState = currentState;
		lastAction = finalAction;
		return finalAction;
	}

	private double storeQ(State state, Action action, double value) {
		return memory.put(state.toString() + action.toString(), value);
	}

	private double Q(State state, Action action) {
		String key = state.toString() + action.toString();
		if (memory.get(key) == null) {
			double initialValue = getInitialValue();
			memory.put(key, initialValue);
			return initialValue;
		} else {
			return memory.get(key);
		}
	}

	private double getInitialValue() {
		return 100;
	}
}
