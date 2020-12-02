package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.reactions.KindOfReactionGroup;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.InputStream;

public class ReactionOperations extends SlackOperations {

    /**
     * This method adds a reaction (emoji) to a message.
     *
     * @param channel   Channel where the message to add reaction to was posted.
     * @param reactionName      Reaction (emoji) name.
     * @param timestamp Timestamp of the message to add reaction to.
     */
    @MediaType(APPLICATION_JSON)
    @DisplayName("Reactions - Add")
    public void addReaction(@Connection SlackConnection slackConnection,
                            String reactionName,
                            String channel,
                            @Example("1405894322.002768") String timestamp,
                            CompletionCallback<Void, Void> callback) {
        slackConnection.reactions.addReaction(channel, reactionName, timestamp)
                .whenCompleteAsync(createConsumer("#[payload]", EXECUTION, callback));
    }

    /**
     * Gets reactions for an item. This operation returns a list of all reactions for a single item
     * (file, file comment, channel message, group message, or direct message).
     *
     * @param channel     Channel where the message to get reactions for was posted.
     * @param file        File to get reactions for.
     * @param fileComment File comment to get reactions for.
     * @param full        If true always return the complete reaction list.
     * @param timestamp   Timestamp of the message to get reactions for.
     */
    @MediaType(APPLICATION_JSON)
    @DisplayName("Reactions - Get")
    @OutputJsonType(schema = "metadata/get-reaction-schema.json")
    public void getReactions(@Connection SlackConnection slackConnection,
                             @ParameterGroup(name = "Reaction Type") KindOfReactionGroup reactionGroup,
                             @Optional(defaultValue = "true") boolean full,
                             CompletionCallback<InputStream, InputStream> callback) {
        slackConnection.reactions.getReaction(reactionGroup.getMessageReaction().getChannel(), 
                reactionGroup.getFileReaction().getFile(),
                reactionGroup.getFileReaction().getFileComment(),
                full,
                reactionGroup.getMessageReaction().getTimestamp())
                .whenCompleteAsync(createConsumer("#[payload - 'ok' - 'type']", "#[payload.'type']", EXECUTION, callback));
    }

    @MediaType(APPLICATION_JSON)
    @DisplayName("Reactions - Remove")
    public void removeReaction(@Connection SlackConnection slackConnection,
                               String reactionName,
                               @ParameterGroup(name = "Reaction Type") KindOfReactionGroup reactionGroup,
                               CompletionCallback<Void, Void> callback) {
        slackConnection.reactions.removeReaction(reactionName,
                reactionGroup.getMessageReaction().getChannel(),
                reactionGroup.getFileReaction().getFile(),
                reactionGroup.getFileReaction().getFileComment(),
                reactionGroup.getMessageReaction().getTimestamp())
                .whenCompleteAsync(createConsumer("#[payload]", EXECUTION, callback));
    }
}
