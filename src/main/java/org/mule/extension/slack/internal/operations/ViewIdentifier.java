package org.mule.extension.slack.internal.operations;

import org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;

@ExclusiveOptionals(isOneRequired = true)
public class ViewIdentifier {

    @Optional
    @Parameter
    @Example("VMM512F2U")
    String viewId;

    @Optional
    @Parameter
    @Example("bmarley_view2")
    String externalId;

    public String getViewId() {
        return viewId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
