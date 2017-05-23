/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;

import org.junit.Test;

import java.util.Arrays;

public class PostMessageWithAttachmentTestCases extends AbstractSlackTestCase {

    @Test
    public void testPostMessageWithAttachments() {
        ChatAttachment chatAttachment = new ChatAttachment();
        chatAttachment.setText("myText");
        chatAttachment.setTitle("myTitle");
        Field field = new Field();
        field.setTitle("myOtherTitle");
        field.setValue("myOtherValue");
        chatAttachment.setFields(Arrays.asList(field));
        String response = getConnector().postMessageWithAttachment(TEST_MESSAGE,CHANNEL_ID,null,null,null,null);
    }

}
