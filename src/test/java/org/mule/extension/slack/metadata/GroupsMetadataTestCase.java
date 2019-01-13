package org.mule.extension.slack.metadata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import org.junit.Test;

public class GroupsMetadataTestCase extends SlackToolingTestCase {

    @Override
    protected String getConfigFile() {
        return "groups-operations-metadata.xml";
    }

    @Test
    public void listGroupsMetadata() {
        MetadataType groupList = getPayloadMetadata("listGroups");
        assertThat(groupList, is(instanceOf(ArrayType.class)));
        ArrayType groups = (ArrayType) groupList;
        assertThat(groups.getType(), is(instanceOf(ObjectType.class)));
        MetadataType groupType = groups.getType();
        assertThat(groupType.getAnnotation(TypeIdAnnotation.class).get().getValue(), is("Group"));
    }

    @Test
    public void groupInfoMetadata() {
        MetadataType groupInfo = getPayloadMetadata("groupInfo");
        assertThat(groupInfo, is(instanceOf(ObjectType.class)));
        assertThat(groupInfo.getAnnotation(TypeAliasAnnotation.class).get().getValue(), is("Group"));
    }

    private ComponentMetadataDescriptor<OperationModel> getMetadata(String flowName) {
        MetadataResult<ComponentMetadataDescriptor<OperationModel>> operationMetadata = metadataService.getOperationMetadata(Location.builder().globalName(flowName).addProcessorsPart().addIndexPart(0).build());
        if(!operationMetadata.isSuccess()) {
            operationMetadata.getFailures().stream().forEach(f -> System.out.println(f.getReason()));
        }
        assertThat(operationMetadata.isSuccess(), is(true));
        return operationMetadata.get();
    }
    private MetadataType getPayloadMetadata(String flowName) {
        ComponentMetadataDescriptor<OperationModel> metadata = getMetadata(flowName);
        return metadata.getModel().getOutput().getType();
    }
}
