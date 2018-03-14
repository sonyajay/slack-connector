package org.mule.extension.slack.internal.connection.category;

import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.DIALOG_OPEN;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class Dialog {

    private SlackConnection slackConnection;

    public Dialog(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> open(InputStream dialog,
                                                       String triggerId) {
        MultiMap<String, String> parameterMap = new MultiMap<>();
        parameterMap.put("trigger_id", triggerId);
        parameterMap.put("dialog", IOUtils.toString(dialog));

        return slackConnection.sendAsyncRequest(API_URI + DIALOG_OPEN, parameterMap);
    }
}
