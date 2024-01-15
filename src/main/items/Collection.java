package main.items;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.filters.Filters;

@Getter
@Setter
@ToString
/**
 * abstract class for all collections
 */
public abstract class Collection extends Item {
    /**
     * owner of the collection
     */
    protected String owner;

    /**
     * @return true if collection is valid
     */
    @Override
    public boolean validate(final Filters filters) {
        if (filters.getName() != null
                && !name.toLowerCase().startsWith(filters.getName().toLowerCase())) {
            return false;
        }
        return filters.getOwner() == null || owner.equals(filters.getOwner());
    }
}
