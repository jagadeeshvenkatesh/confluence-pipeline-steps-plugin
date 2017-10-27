package de.sprengnetter.confluencesteps;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Enum to declare the type of authentication.
 */
public enum AuthenticationType {
    BASIC("basic"), OAUTH("oauth");

    private String type;

    /**
     * Constructor which takes a {@link String} representation of the enum constant.
     *
     * @param type
     *        The {@link String} representation of the enum.
     */
    AuthenticationType(final String type) {
        this.type = type;
    }

    /**
     * Returns an enum constant from a given {@link String}.
     *
     * @param value
     *        The {@link String} representation of the desired enum constant.
     * @return The desired enum constant or {@code null}, if no matching type could be found.
     */
    public static AuthenticationType fromString(final String value) {
        if (value == null) return null;
        for (AuthenticationType type : values()) {
            if (value.equals(type.name()) || value.equals(type.type)) return type;
        }
        return null;
    }

    /**
     * Returns a {@link String} representation of the enum constant.
     *
     * @return The {@link String} representation of the enum constant.
     */
    public String getType() {
        return type;
    }
}
