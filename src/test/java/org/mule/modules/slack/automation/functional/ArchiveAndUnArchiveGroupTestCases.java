/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import org.junit.Test;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by estebanwasinger on 8/8/15.
 */
public class ArchiveAndUnArchiveGroupTestCases extends AbstractSlackTestCase {

    @Test
    public void testArchiveAndUnarchiveChannel() {
        if (!getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived()) {
            getConnector().archiveChannel(CHANNEL_RENAMING);
            assertEquals(true, getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
        }
        getConnector().unarchiveChannel(CHANNEL_RENAMING);
        assertEquals(false, getConnector().getChannelInfo(CHANNEL_RENAMING).getIsArchived());
    }

}