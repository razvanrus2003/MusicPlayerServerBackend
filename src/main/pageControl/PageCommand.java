package main.pageControl;

public interface PageCommand {
    public void execute();

    public void undo();
}
