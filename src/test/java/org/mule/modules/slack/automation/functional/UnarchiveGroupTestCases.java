package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;

public class UnarchiveGroupTestCases extends AbstractSlackTestCase {

    @Before
    public void setUp() {
        if (!getConnector().getGroupInfo(GROUP_ID).getIsArchived()) {
            getConnector().archiveGroup(GROUP_ID);
        }
    }

    @Test
    public void unarchiveGroup() {
        getConnector().unarchiveGroup(GROUP_ID);
        assertEquals(false, getConnector().getGroupInfo(GROUP_ID).getIsArchived());
    }
}
