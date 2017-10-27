package de.sprengnetter.confluencesteps.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Body implements Serializable {

    private static final long serialVersionUID = -3817236910371890302L;

    @JsonProperty("storage")
    private Storage storage;

    private Map<String, Object> unmappedFields = new HashMap<>();

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @JsonAnyGetter
    public Map<String, Object> getUnmappedFields() {
        return unmappedFields;
    }

    @JsonAnySetter
    public void setUnmappedFields(final String name, final Object o) {
        unmappedFields.put(name, o);
    }

    @Override
    public String toString() {
        return "Body{" +
            "storage=" + storage +
            ", unmappedFields=" + unmappedFields +
            '}';
    }
}
