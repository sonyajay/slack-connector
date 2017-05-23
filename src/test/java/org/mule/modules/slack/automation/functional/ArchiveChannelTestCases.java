/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;
import org.mule.modules.slack.client.model.channel.Channel;

import org.junit.Before;
import org.junit.Test;

public class ArchiveChannelTestCases extends AbstractSlackTestCase {

    public static Channel channel;

    @Before
    public void setUp() {
        channel = getConnector().createChannel(String.valueOf(Math.random()));

        if (getConnector().getChannelInfo(channel.getId()).getIsArchived()) {
            getConnector().unarchiveChannel(channel.getId());
        }
    }

    @Test
    public void archiveChannel() {
        getConnector().archiveChannel(channel.getId());
        assertEquals(true, getConnector().getChannelInfo(channel.getId()).getIsArchived());
    }
}
