package org.mule.extension.slack.internal.operations.reactions;

import org.mule.extension.slack.internal.connection.category.File;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class KindOfReactionGroup {

    @Parameter
    @Optional
    @NullSafe
    @ParameterDsl(allowReferences = false)
    @Expression(ExpressionSupport.NOT_SUPPORTED)
    FileReactionGroup fileReaction;

    @Parameter
    @Optional
    @NullSafe
    @Expression(ExpressionSupport.NOT_SUPPORTED)
    @ParameterDsl(allowReferences = false)
    MessageReactionGroup messageReaction;

    public FileReactionGroup getFileReaction() {
        if(fileReaction == null) {
            fileReaction = new FileReactionGroup();
        }
        return fileReaction;
    }

    public MessageReactionGroup getMessageReaction() {
        if(messageReaction == null) {
            messageReaction = new MessageReactionGroup();
        }
        return messageReaction;
    }
}
