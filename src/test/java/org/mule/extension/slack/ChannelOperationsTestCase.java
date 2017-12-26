package org.mule.extension.slack;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.core.api.event.CoreEvent;

import javax.inject.Inject;

import java.util.Map;

import org.junit.Test;

public class ChannelOperationsTestCase extends MuleArtifactFunctionalTestCase {

    @Inject
    ExpressionManager expressionManager;

    @Override
    protected String getConfigFile() {
        return "channel-operations.xml";
    }

    @Test
    public void getChannelInfo() throws Exception {
        CoreEvent event = flowRunner("channelInfo").withPayload("C03NE28RY").keepStreamsOpen().run();
        TypedValue<Map<String, Object>> evaluate = expressionManager.evaluate("#[output application/java --- payload]", event);

        Map<String, Object> value = evaluate.getValue();
        assertThat(value, allOf(hasEntry("id", "C03NE28RY"), hasEntry("name", "random")));
    }
}
