package org.mule.extension.slack.metadata;

import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import org.junit.Test;

public class ChannelMetadataTestCase extends SlackToolingTestCase {

    @Override
    protected String getConfigFile() {
        return "channel-operations-metadata.xml";
    }

    @Test
    public void channelInfoMetadata() {
        MetadataResult<ComponentMetadataDescriptor<OperationModel>> channelInfo = metadataService.getOperationMetadata(Location.builder().globalName("channelInfo").addProcessorsPart().addIndexPart(0).build());
        OperationModel model = channelInfo.get().getModel();
        System.out.println(model);
    }
}
