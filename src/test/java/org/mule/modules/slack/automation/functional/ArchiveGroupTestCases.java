/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;

public class ArchiveGroupTestCases extends AbstractSlackTestCase {

    @Before
    public void setUp(){
        if (getConnector().getGroupInfo(GROUP_ID).getIsArchived()) {
            getConnector().unarchiveGroup(GROUP_ID);
        }
    }

    @Test
    public void archiveGroup() {
        getConnector().archiveGroup(GROUP_ID);
        assertEquals(false, getConnector().getGroupInfo(GROUP_ID).getIsArchived());
    }
}
