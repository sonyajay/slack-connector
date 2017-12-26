package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

public class ExecutionErrorTypeProvider extends BaseErrorTypeProvider {

    @Override
    public void addErrors(Set<ErrorTypeDefinition> errors) {
        errors.add(EXECUTION);
    }
}
