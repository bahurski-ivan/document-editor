package ru.sbrf.docedit.model.user;

import java.util.EnumMap;
import java.util.Map;

import static ru.sbrf.docedit.model.user.Action.*;

/**
 * Represents user's role.
 */
public enum Role {
    REGULAR,
    ADMINISTRATOR;

    private final static Map<Role, Map<Action, Boolean>> accessMap;

    static {
        accessMap = new EnumMap<>(Role.class);
        final Map<Action, Boolean> regularMap = new EnumMap<>(Action.class);
        final Map<Action, Boolean> adminMap = new EnumMap<>(Action.class);

        regularMap.put(CREATE_DOCUMENT, true);
        regularMap.put(DELETE_DOCUMENT, false);
        regularMap.put(EDIT_DOCUMENT, true);
        regularMap.put(CREATE_TEMPLATE, false);
        regularMap.put(DELETE_TEMPLATE, false);
        regularMap.put(EDIT_TEMPLATE, false);

        adminMap.put(CREATE_DOCUMENT, true);
        adminMap.put(DELETE_DOCUMENT, true);
        adminMap.put(EDIT_DOCUMENT, true);
        adminMap.put(CREATE_TEMPLATE, true);
        adminMap.put(DELETE_TEMPLATE, true);
        adminMap.put(EDIT_TEMPLATE, true);

        accessMap.put(ADMINISTRATOR, adminMap);
        accessMap.put(REGULAR, regularMap);
    }

    public boolean allowedToPerform(Action action) {
        final Map<Action, Boolean> m = accessMap.get(this);
        return m != null && m.containsKey(action) ? m.get(action) : false;
    }
}
