/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrieveEventsTestCases extends AbstractSlackTestCase {

    private static final String TEST_MESSAGE = "Test";
    private static final String TEXT_PROPERTY = "text";
    private static final String RETRIEVE_EVENTS_SOURCE = "retrieveEvents";

    @Before
    public void setUp() throws Throwable{
        Object[] signature = {null, true, false, false, true, false, false, false, false, false, false, null, null};
        getDispatcher().initializeSource(RETRIEVE_EVENTS_SOURCE, signature);
    }

    @Test
    public void testSource() throws InterruptedException {
        getConnector().postMessage(TEST_MESSAGE, CHANNEL_ID, null, null, true);
        Thread.sleep(5000);
        List<Object> result = getDispatcher().getSourceMessages(RETRIEVE_EVENTS_SOURCE);
        assertTrue(!result.isEmpty());
        Map<String, Object> event = (Map<String, Object>) result.get(0);
        assertEquals(TEST_MESSAGE, event.get(TEXT_PROPERTY));
    }

    @After
    public void tearDown() throws Throwable{
        getDispatcher().shutDownSource(RETRIEVE_EVENTS_SOURCE);
    }
}
