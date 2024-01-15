package main.pageControl;

/**
 * PageCommand interface
 * Used to implement the command pattern for page changes
 */
public interface PageCommand {
    /**
     * Executes the command
     */
    void execute();
    /**
     * Undoes the command
     */
    void undo();
}
