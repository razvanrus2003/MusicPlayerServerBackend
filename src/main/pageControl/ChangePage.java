package main.pageControl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.User;

@Getter
@Setter
@ToString
/**
 * ChangePage class
 * Used to implement the command pattern for page changes
 */
public class ChangePage implements PageCommand {
    private User user;
    private User nextPageOwner;
    private String nextType;
    private User prevPageOwner;
    private String prevType;

    public ChangePage(final User user, final User nextPageOwner, final String nextType) {
        this.user = user;
        this.nextPageOwner = nextPageOwner;
        this.nextType = nextType;
    }

    /**
     * Executes the command by changing the page and page owner the next values
     */
    @Override
    public void execute() {
        prevPageOwner = user.getPageOwner();
        prevType = user.getPage();

        user.setPage(nextType);
        user.setPageOwner(nextPageOwner);
    }

    /**
     * Undoes the command by changing the page and page owner to the previous values
     */
    @Override
    public void undo() {
        user.setPage(prevType);
        user.setPageOwner(prevPageOwner);
    }
}
