package org.mule.extension.slack.internal;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class ConversationListingConfiguration {

    @Parameter
    @Optional
    private String cursor;

    @Parameter
    @Optional(defaultValue = "100")
    private String limit;

    @Parameter
    @Optional(defaultValue = "false")
    private boolean excludeArchive;

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isExcludeArchive() {
        return excludeArchive;
    }

    public void setExcludeArchive(boolean excludeArchive) {
        this.excludeArchive = excludeArchive;
    }
}
