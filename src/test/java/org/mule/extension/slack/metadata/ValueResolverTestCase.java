package org.mule.extension.slack.metadata;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.tck.junit4.matcher.ValueMatcher.valueWithId;

import org.mule.runtime.api.component.location.Location;
import org.mule.runtime.api.value.ValueResult;

import org.junit.Test;

public class ValueResolverTestCase extends SlackToolingTestCase {

    @Override
    protected String getConfigFile() {
        return "value-providers.xml";
    }

    @Test
    public void getChannelsValueProvider() {
        ValueResult values = valueProviderService.getValues(Location.builder().globalName("channelInfo").addProcessorsPart().addIndexPart(0).build(), "channel");
        assertThat(values.isSuccess(), is(true));
        assertThat(values.getValues(), hasItems(valueWithId("C03NE28RY").withDisplayName("random")));
    }

    @Test
    public void getUsersValueProvider() {
        ValueResult values = valueProviderService.getValues(Location.builder().globalName("inviteToChannel").addProcessorsPart().addIndexPart(0).build(), "user");
        assertThat(values.isSuccess(), is(true));
        assertThat(values.getValues(), hasItems(valueWithId("U03NE28RL").withDisplayName("estebanwasinger")));
    }

    @Test
    public void getGroupsValueProvider() {
        ValueResult values = valueProviderService.getValues(Location.builder().globalName("groupInfo").addProcessorsPart().addIndexPart(0).build(), "channel");
        assertThat(values.isSuccess(), is(true));
        assertThat(values.getValues(), hasItems(valueWithId("G03R6ABL2").withDisplayName("somegroup")));
    }

    @Test
    public void getChannelsPostMessage() {
        ValueResult values = valueProviderService.getValues(Location.builder().globalName("postMessage").addProcessorsPart().addIndexPart(0).build(), "channel");
        assertThat(values.isSuccess(), is(true));
        assertThat(values.getValues(), hasItems(valueWithId("G03R6ABL2").withDisplayName("somegroup")));
    }


}
