package com.assignment.progresssoft.dto.response;

import java.util.Objects;
import java.util.Set;

public class AccessibleIdsResponse {
    private Set<Long> accessibleIds;

    public AccessibleIdsResponse() {
    }

    public AccessibleIdsResponse(Set<Long> accessibleIds) {
        this.accessibleIds = accessibleIds;
    }

    public Set<Long> getAccessibleIds() {
        return accessibleIds;
    }

    public void setAccessibleIds(Set<Long> accessibleIds) {
        this.accessibleIds = accessibleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessibleIdsResponse that = (AccessibleIdsResponse) o;
        return Objects.equals(accessibleIds, that.accessibleIds);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accessibleIds);
    }

    @Override
    public String toString() {
        return "AccessibleIdsResponse{" +
                "accessibleIds=" + accessibleIds +
                '}';
    }
}
