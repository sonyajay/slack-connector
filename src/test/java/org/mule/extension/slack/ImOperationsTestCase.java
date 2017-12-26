package org.mule.extension.slack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ImOperationsTestCase extends MuleArtifactFunctionalTestCase {

    private static final String USER_ID = "U04RULH9A";

    @Override
    protected String getConfigFile() {
        return "im-operations.xml";
    }

    @Test
    public void listIms() throws Exception {
        List<Map<String, Object>> listIms = (List<Map<String, Object>>) flowRunner("listIms").run().getMessage().getPayload().getValue();
        Matcher withUserEntry = hasEntry("user", USER_ID);
        assertThat(listIms, hasItem(withUserEntry));
    }

    @Test
    public void openIm() throws Exception {
        Map<String, Object> openIm = (Map<String, Object>) flowRunner("openIm").withPayload(USER_ID).run().getMessage().getPayload().getValue();
        assertThat(openIm, hasEntry("id", "D04RULHG6"));
    }
}
