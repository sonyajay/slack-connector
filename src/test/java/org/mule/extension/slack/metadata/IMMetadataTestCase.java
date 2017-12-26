package org.mule.extension.slack.metadata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mule.tck.junit4.matcher.MetadataKeyMatcher.metadataKeyWithId;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Set;

import org.junit.Test;

public class IMMetadataTestCase extends SlackToolingTestCase {


    @Override
    protected String getConfigFile() {
        return "im-operations-metadata.xml";
    }

    @Test
    public void getOpenIMMetadataKeys() {
        MetadataResult<MetadataKeysContainer> openIm = metadataService.getMetadataKeys(Location.builder().globalName("openIm").addProcessorsPart().addIndexPart(0).build());
        assertThat(openIm.isSuccess(), is(true));
        Set<MetadataKey> metadataKeys = openIm.get().getKeys("open-im").get();
        assertThat(metadataKeys, hasItems(metadataKeyWithId("TRUE"), metadataKeyWithId("FALSE")));
    }

    @Test
    public void getOpenImOutput() {
        MetadataResult<ComponentMetadataDescriptor<OperationModel>> openIm = metadataService.getOperationMetadata(Location.builder().globalName("openIm").addProcessorsPart().addIndexPart(0).build());
        assertThat(openIm.isSuccess(), is(true));
        MetadataType type = openIm.get().getModel().getOutput().getType();
        System.out.println(type);
    }

    @Test
    public void getOpenImWithReturnImOutput() {
        MetadataResult<ComponentMetadataDescriptor<OperationModel>> openIm = metadataService.getOperationMetadata(Location.builder().globalName("openImWithReturnIm").addProcessorsPart().addIndexPart(0).build());
        assertThat(openIm.isSuccess(), is(true));
        MetadataType type = openIm.get().getModel().getOutput().getType();
        System.out.println(type);
    }
}
