package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.DESCRIBING;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

public class DescribingErrorProvider extends BaseErrorTypeProvider {

    @Override
    public void addErrors(Set<ErrorTypeDefinition> errors) {
        errors.add(DESCRIBING);
    }
}
