package infrastructure.out;

import infrastructure.out.state.ConsoleState;
import infrastructure.out.state.main.MainState;

/**
 * The class ApplicationLogic.
 */
public class ApplicationLogic {
    private ConsoleState state;

    public ApplicationLogic() {
        this.state = new MainState();
    }

    /**
     * The method which initials a console state.
     * While method nextState() will return a not null
     * method will work.
     */
    public void process() {
        while (true) {
            try {
                state.process();
                if (state.nextState() != null) {
                    ConsoleState nextState = state.nextState();
                    state = nextState;
                }
            } catch (Exception e) {
                System.err.println("Problem with input: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}