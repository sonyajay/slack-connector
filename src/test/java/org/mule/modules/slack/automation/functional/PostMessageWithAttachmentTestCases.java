/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.slack.automation.functional;

import static org.junit.Assert.assertEquals;

import org.mule.modules.slack.automation.runner.AbstractSlackTestCase;
import org.mule.modules.slack.client.model.chat.attachment.ChatAttachment;
import org.mule.modules.slack.client.model.chat.attachment.Field;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostMessageWithAttachmentTestCases extends AbstractSlackTestCase {

    private Gson gson = new Gson();

    @Test
    public void testPostMessageWithAttachments() {
        ChatAttachment chatAttachment = new ChatAttachment();
        chatAttachment.setText("myText");
        chatAttachment.setTitle("myTitle");
        Field field = new Field();
        field.setTitle("myOtherTitle");
        field.setValue("myOtherValue");
        chatAttachment.setFields(Arrays.asList(field));
        ArrayList<ChatAttachment> attachments = new ArrayList<>();
        attachments.add(chatAttachment);
        String sentAttachments = gson.toJson(attachments);
        String response = getConnector().postMessageWithAttachment(TEST_MESSAGE,CHANNEL_ID,null,null, sentAttachments,null);
        JSONArray jsonAttachments = new JSONObject(response).getJSONObject("message").getJSONArray("attachments");
        System.out.println(jsonAttachments);
    }

}
