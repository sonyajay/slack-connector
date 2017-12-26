package org.mule.extension.slack.internal.metadata;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;
import static org.mule.extension.slack.internal.utils.SlackUtils.getBindingContext;
import static org.mule.runtime.extension.api.values.ValueBuilder.newValue;

import org.mule.extension.slack.internal.ConversationListingConfiguration;
import org.mule.extension.slack.internal.ConversationTypes;
import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

//TODO - Si esto se convierte a un KeysResolver se mezclan los resolvers
public class ChannelsKeyResolver implements ValueProvider {

    @Connection
    SlackConnection slackConnection;

    @Override
    public Set<Value> resolve() {
        Reference<List<Map<String, String>>> channels = new Reference<>();
        Reference<List<Map<String, String>>> groups = new Reference<>();
        Reference<List<Map<String, String>>> conversations = new Reference<>();
        Reference<Map<String, String>> users = new Reference<>();
        CountDownLatch countDownLatch = new CountDownLatch(2);

        MetadataResponseConsumerFactory factory = new MetadataResponseConsumerFactory(countDownLatch, slackConnection);
//
//        slackConnection.channel.list(null, true, true, 0)
//                .whenCompleteAsync(factory.createConsumer(
//                    "#[output application/java --- payload.channels map {name : $.name, id : $.id}]",
//                        channels, () -> channels.set(emptyList())));
////
//        slackConnection.group.list(true, true)
//                .whenCompleteAsync(factory.createConsumer(
//                        "#[output application/java --- payload.groups map {name : $.name, id : $.id}]",
//                        groups,
//                        () -> groups.set(emptyList())));

        slackConnection.user.list(null, false, 0, false)
                .whenCompleteAsync(factory.createConsumer(
                        "#[output application/java --- payload.members reduce ((user, acc = {}) -> acc ++ {'$(user.id)' : user.name })]",
                        users,
                        () -> users.set(emptyMap())));

        ConversationTypes conversationTypes = new ConversationTypes();
        conversationTypes.setIm(true);
        conversationTypes.setPrivateChannels(true);
        conversationTypes.setPublicChannels(true);

        ConversationListingConfiguration listingConfiguration = new ConversationListingConfiguration();
        listingConfiguration.setExcludeArchive(true);
        listingConfiguration.setLimit("2000");

        slackConnection.conversationList(conversationTypes, listingConfiguration)
                .whenCompleteAsync(factory.createConsumer(
                        "#[output application/java --- payload.channels map {name : $.name, id : $.id, user : $.user}]",
                        conversations,
                        () -> conversations.set(emptyList())));


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BindingContext bindingContext = BindingContext.builder().addBinding("payload", typedValueOf(conversations)).addBinding("names", typedValueOf(users)).build();
        List<Map<String, String>> allConversations = (List<Map<String, String>>) slackConnection.getExpressionManager().evaluate("payload map {name : if($.name == null) names[$.user] else $.name,id : $.id}", bindingContext).getValue();

        return allConversations.stream()
                .map((Map<String, String> map) -> newValue(map.get("id")).withDisplayName(map.get("name")))
                .map(ValueBuilder::build)
                .collect(toSet());
    }

    private TypedValue typedValueOf(Reference conversations) {
        return new TypedValue(conversations.get(), DataType.fromType(conversations.get().getClass()));
    }

    public class MetadataResponseConsumerFactory {
        private final CountDownLatch countDownLatch;
        private final SlackConnection slackConnection;

        public MetadataResponseConsumerFactory(CountDownLatch countDownLatch, SlackConnection slackConnection) {
            this.countDownLatch = countDownLatch;
            this.slackConnection = slackConnection;
        }

        public <T> MetadataResponseConsumer<T> createConsumer(String expression, Reference<T> output, Runnable onError) {
            return new MetadataResponseConsumer<>(expression, output, onError, countDownLatch, slackConnection);
        }
    }

    public class MetadataResponseConsumer<T> implements BiConsumer<HttpResponse, Throwable> {

        private String expression;
        private Reference<T> output;
        private Runnable onError;
        private CountDownLatch countDownLatch;
        private SlackConnection slackConnection;

        public MetadataResponseConsumer(String expression, Reference<T> output, Runnable onError, CountDownLatch countDownLatch, SlackConnection slackConnection) {
            this.expression = expression;
            this.output = output;
            this.onError = onError;
            this.countDownLatch = countDownLatch;
            this.slackConnection = slackConnection;
        }

        @Override
        public void accept(HttpResponse httpResponse, Throwable throwable) {
            if (throwable != null) {
                onError.run();
                return;
            }
            try {
                TypedValue<?> evaluate = slackConnection.getExpressionManager().evaluate(expression, getBindingContext(httpResponse.getEntity().getContent()));
                output.set((T) evaluate.getValue());
                countDownLatch.countDown();
            } catch (Throwable e) {
                onError.run();
            }
        }
    }
}
