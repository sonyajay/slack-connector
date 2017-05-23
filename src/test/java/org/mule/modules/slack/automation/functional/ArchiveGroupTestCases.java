/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;
import org.mule.modules.slack.client.model.group.Group;

import org.junit.Before;
import org.junit.Test;

public class ArchiveGroupTestCases extends AbstractSlackTestCase {

    private static Group group;

    @Before
    public void setUp(){
        group = getConnector().createGroup(String.valueOf(Math.random()));
        
        if (getConnector().getGroupInfo(group.getId()).getIsArchived()) {
            getConnector().unarchiveGroup(group.getId());
        }
    }

    @Test
    public void archiveGroup() {
        getConnector().archiveGroup(group.getId());
        assertEquals(true, getConnector().getGroupInfo(group.getId()).getIsArchived());
    }
}
