package org.mule.extension.slack.internal.valueprovider;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.operations.ChannelOperations;
import org.mule.extension.slack.internal.operations.ConversationOperations;
import org.mule.runtime.core.api.el.ExpressionManager;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.util.Map;

public class ConversationsValueProvider extends BaseValueProvider {

    public ConversationsValueProvider() {
        super("id", "name");
    }

    public ConversationsValueProvider(ExpressionManager expressionManager, SlackConnection slackConnection) {
        super("id", "name", expressionManager, slackConnection);
    }

    boolean publicChannels = true;
    boolean privateChannels = true;
    boolean mpim = true;
    boolean im = false;
    boolean excludeArchived = true;

    @Override
    PagingProvider<SlackConnection, Map<String, Object>> getPagingProvider() {
        ConversationOperations conversations = new ConversationOperations();
        conversations.setExpressionManager(expressionManager);
        return conversations.listConversations(publicChannels, privateChannels, mpim, im, excludeArchived);
    }

    public void setPublicChannels(boolean publicChannels) {
        this.publicChannels = publicChannels;
    }

    public void setPrivateChannels(boolean privateChannels) {
        this.privateChannels = privateChannels;
    }

    public void setMpim(boolean mpim) {
        this.mpim = mpim;
    }

    public void setIm(boolean im) {
        this.im = im;
    }

    public void setExcludeArchived(boolean excludeArchived) {
        this.excludeArchived = excludeArchived;
    }
}
