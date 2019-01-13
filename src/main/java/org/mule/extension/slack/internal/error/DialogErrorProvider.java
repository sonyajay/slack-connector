package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.extension.slack.internal.error.SlackError.TRIGGER_EXPIRED;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

public class DialogErrorProvider extends BaseErrorTypeProvider {

    @Override
    public void addErrors(Set<ErrorTypeDefinition> errors) {
        errors.add(EXECUTION);
        errors.add(TRIGGER_EXPIRED);
    }
}
