package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.MessageConfigurationGroup;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.ExecutionErrorTypeProvider;
import org.mule.extension.slack.internal.error.PostMessageErrorProvider;
import org.mule.extension.slack.internal.metadata.AttachmentsTypeResolver;
import org.mule.extension.slack.internal.metadata.PostMessageAttributesResolver;
import org.mule.extension.slack.internal.metadata.PostMessageOutputResolver;
import org.mule.extension.slack.internal.metadata.ViewInputTypeResolver;
import org.mule.extension.slack.internal.valueprovider.ChannelsKeyResolver;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

@Throws(ExecutionErrorTypeProvider.class)
public class ViewsOperations extends SlackOperations {

    @MediaType(APPLICATION_JSON)
    @DisplayName("Views - Open")
    public void openView(@Connection SlackConnection slackConnection,
                         @Example("12345.98765.abcd2358fdea") String triggerId,
                         @TypeResolver(ViewInputTypeResolver.class) InputStream view,
                         CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.views
                .open(view, triggerId)
                .whenCompleteAsync(createConsumer("#[payload.view]", "#[payload - 'ok']", EXECUTION, callback));
    }

    @MediaType(APPLICATION_JSON)
    @DisplayName("Views - Push")
    public void pushView(@Connection SlackConnection slackConnection,
                         @Example("12345.98765.abcd2358fdea") String triggerId,
                         @TypeResolver(ViewInputTypeResolver.class) InputStream view,
                         CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.views
                .push(view, triggerId)
                .whenCompleteAsync(createConsumer("#[payload.view]", "#[payload - 'ok']", EXECUTION, callback));
    }

    @MediaType(APPLICATION_JSON)
    @DisplayName("Views - Publish")
    public void publishView(@Connection SlackConnection slackConnection,
                            @Example("U0BPQUNTA") String userId,
                            @TypeResolver(ViewInputTypeResolver.class) InputStream view,
                            @Optional @Example("156772938.1827394") String hash,
                            CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.views
                .publish(view, userId, hash)
                .whenCompleteAsync(createConsumer("#[payload.view]", "#[payload - 'ok']", EXECUTION, callback));
    }


    @MediaType(APPLICATION_JSON)
    @DisplayName("Views - Update")
    public void updateView(@Connection SlackConnection slackConnection,
                           @ParameterGroup(name = "View Identifier") ViewIdentifier identifier,
                           @TypeResolver(ViewInputTypeResolver.class) InputStream view,
                           @Optional @Example("156772938.1827394") String hash,
                           CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.views
                .update(view, identifier.externalId, hash, identifier.viewId)
                .whenCompleteAsync(createConsumer("#[payload.view]", "#[payload - 'ok']", EXECUTION, callback));
    }
}
