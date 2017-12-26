package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.CHANNEL_LISTING;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

public class ChannelListErrorProvider extends BaseErrorTypeProvider {

    @Override
    public void addErrors(Set<ErrorTypeDefinition> errors) {
        errors.add(CHANNEL_LISTING);
    }
}
