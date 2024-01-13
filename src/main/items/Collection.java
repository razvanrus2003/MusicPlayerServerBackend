package main.items;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.MusicPlayer;
import main.User;
import main.filters.Filters;

@Getter
@Setter
@ToString
public abstract class Collection extends Item {
    /**
     *
     */
    protected String owner;

    /**
     * for coding style
     */
    @Override
    public boolean validate(final Filters filters) {
        if (filters.getName() != null
                && !name.toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        if (filters.getOwner() != null && !owner.equals(filters.getOwner())) {
            return false;
        }
        return true;
    }

    public abstract Item next(MusicPlayer musicPlayer, int timestamp);
}
