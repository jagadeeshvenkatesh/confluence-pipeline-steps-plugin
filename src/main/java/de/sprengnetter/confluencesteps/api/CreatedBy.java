package de.sprengnetter.confluencesteps.api;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatedBy {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("userKey")
    private String userKey;

    @JsonIgnore
    private final Map<String, Object> unmappedFields = new HashMap<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(String name, Object o) {
        this.unmappedFields.put(name, o);
    }

    @Override
    public String toString() {
        return "CreatedBy{" +
            "userName='" + userName + '\'' +
            ", displayName='" + displayName + '\'' +
            ", userKey='" + userKey + '\'' +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}
