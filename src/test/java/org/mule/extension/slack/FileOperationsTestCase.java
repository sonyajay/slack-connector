package org.mule.extension.slack;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.event.CoreEvent;

import java.util.Map;

import org.junit.Test;

public class FileOperationsTestCase extends MuleArtifactFunctionalTestCase {

    @Override
    protected String getConfigFile() {
        return "file-operations.xml";
    }

    @Test
    public void uploadFile() throws Exception {
        CoreEvent event = flowRunner("uploadFile").withPayload(singletonList("C03NE28RY")).withVariable("content", "{}", DataType.builder().type(String.class).mediaType("application/json").build()).run();
        Map<String, Object> value = (Map<String, Object>) event.getMessage().getPayload().getValue();
        assertThat(value, hasEntry("pretty_type", "JavaScript/JSON"));
    }

}
