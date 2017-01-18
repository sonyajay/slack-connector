package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;

public class UnarchiveChannelTestCases extends AbstractSlackTestCase {

    @Before
    public void setUp() {
        if (!getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived()) {
            getConnector().archiveChannel(CHANNEL_RENAMING);
        }
    }

    @Test
    public void unarchiveChannel() {
        getConnector().unarchiveChannel(CHANNEL_RENAMING);
        assertEquals(false, getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
    }
}