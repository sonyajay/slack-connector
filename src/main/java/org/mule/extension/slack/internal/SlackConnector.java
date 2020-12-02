package org.mule.extension.slack.internal;


import static org.mule.runtime.api.meta.Category.COMMUNITY;

import org.mule.extension.slack.api.RequestResponse;
import org.mule.extension.slack.internal.connection.OAuth2ConnectionProvider;
import org.mule.extension.slack.internal.connection.TokenConnectionProvider;
import org.mule.extension.slack.internal.error.ExecutionErrorTypeProvider;
import org.mule.extension.slack.internal.error.SlackError;
import org.mule.extension.slack.internal.operations.ChannelOperations;
import org.mule.extension.slack.internal.operations.ChatOperations;
import org.mule.extension.slack.internal.operations.ConversationOperations;
import org.mule.extension.slack.internal.operations.DialogOperations;
import org.mule.extension.slack.internal.operations.FileOperations;
import org.mule.extension.slack.internal.operations.GroupOperations;
import org.mule.extension.slack.internal.operations.IMOperations;
import org.mule.extension.slack.internal.operations.ReactionOperations;
import org.mule.extension.slack.internal.operations.UsersOperations;
import org.mule.extension.slack.internal.operations.ViewsOperations;
import org.mule.extension.slack.internal.source.OnInteractiveAction;
import org.mule.extension.slack.internal.source.OnSlashCommand;
import org.mule.extension.slack.internal.source.SlackMessageReceiver;
import org.mule.runtime.extension.api.annotation.Export;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.runtime.extension.api.annotation.error.Throws;

@Extension(name = "Slack", vendor = "Esteban Wasinger", category = COMMUNITY)
@ConnectionProviders({TokenConnectionProvider.class, OAuth2ConnectionProvider.class})
@Operations({ChannelOperations.class,
        ChatOperations.class,
        GroupOperations.class,
        UsersOperations.class,
        FileOperations.class,
        IMOperations.class,
        DialogOperations.class,
        ViewsOperations.class,
        ReactionOperations.class,
        ConversationOperations.class})
@Sources({SlackMessageReceiver.class, OnSlashCommand.class, OnInteractiveAction.class})
@ErrorTypes(SlackError.class)
@Throws(ExecutionErrorTypeProvider.class)
@Export(classes = RequestResponse.class, resources = "Slack/DateUtils.dwl")
@Xml(prefix = "slack")
public class SlackConnector {
}
