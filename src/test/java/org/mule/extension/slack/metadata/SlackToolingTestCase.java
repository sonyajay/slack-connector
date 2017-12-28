package org.mule.extension.slack.metadata;

import static org.mule.runtime.api.connectivity.ConnectivityTestingService.CONNECTIVITY_TESTING_SERVICE_KEY;
import static org.mule.runtime.api.metadata.MetadataService.METADATA_SERVICE_KEY;
import static org.mule.runtime.api.value.ValueProviderService.VALUE_PROVIDER_SERVICE_KEY;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.connectivity.ConnectivityTestingService;
import org.mule.runtime.api.metadata.MetadataService;
import org.mule.runtime.api.value.ValueProviderService;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class SlackToolingTestCase extends MuleArtifactFunctionalTestCase {

    @Inject
    @Named(VALUE_PROVIDER_SERVICE_KEY)
    ValueProviderService valueProviderService;

    @Inject
    @Named(METADATA_SERVICE_KEY)
    MetadataService metadataService;

    @Inject
    @Named(CONNECTIVITY_TESTING_SERVICE_KEY)
    ConnectivityTestingService connectivityTestingService;

    @Override
    public boolean enableLazyInit() {
        return true;
    }

    @Override
    public boolean disableXmlValidations() {
        return true;
    }

    @Override
    protected boolean isDisposeContextPerClass() {
        return true;
    }
}
