package infrastructure.out.state;

/**
 * This interface provides a contract for classes which implementations console state.
 */
public interface ConsoleState {
    void process() throws Exception;
    ConsoleState nextState();
}