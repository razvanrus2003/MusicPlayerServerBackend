package main.pageControl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.User;

@Getter
@Setter
@ToString
public class ChangePage implements PageCommand {
    private User user;

    private User nextPageOwner;
    private String nextType;

    private User prevPageOwner;
    private String prevType;

    public ChangePage(User user, User nextPageOwner, String nextType) {
        this.user = user;
        this.nextPageOwner = nextPageOwner;
        this.nextType = nextType;
    }

    @Override
    public void execute() {
        prevPageOwner = user.getPageOwner();
        prevType = user.getPage();

        user.setPage(nextType);
        user.setPageOwner(nextPageOwner);
    }

    @Override
    public void undo() {
        user.setPage(prevType);
        user.setPageOwner(prevPageOwner);
    }
}
