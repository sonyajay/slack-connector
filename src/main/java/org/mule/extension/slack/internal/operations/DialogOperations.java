package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.PostMessageErrorProvider;
import org.mule.extension.slack.internal.metadata.DialogInputTypeResolver;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class DialogOperations extends SlackOperations {

    @Throws(PostMessageErrorProvider.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("Dialog - Open")
    public void openDialog(@Connection SlackConnection slackConnection,
                            String triggerId,
                            @Content @TypeResolver(value = DialogInputTypeResolver.class) InputStream dialog,
                            CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.dialog.open(dialog, triggerId)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload]", EXECUTION, callback));
    }

}
