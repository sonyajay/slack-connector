package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.ACCOUNT_INACTIVE;
import static org.mule.extension.slack.internal.error.SlackError.INVALID_AUTH;
import static org.mule.extension.slack.internal.error.SlackError.NOT_AUTHED;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseErrorTypeProvider implements ErrorTypeProvider {

    public abstract void addErrors(Set<ErrorTypeDefinition> errors);

    @Override
    public final Set<ErrorTypeDefinition> getErrorTypes() {
        HashSet<ErrorTypeDefinition> errors = new HashSet<>();
        errors.add(NOT_AUTHED);
        errors.add(INVALID_AUTH);
        errors.add(ACCOUNT_INACTIVE);
        addErrors(errors);
        return errors;
    }
}