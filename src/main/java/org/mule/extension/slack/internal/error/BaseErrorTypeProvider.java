package org.mule.extension.slack.internal.error;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseErrorTypeProvider implements ErrorTypeProvider {

    public abstract void addErrors(Set<ErrorTypeDefinition> errors);

    @Override
    public final Set<ErrorTypeDefinition> getErrorTypes() {
        HashSet<ErrorTypeDefinition> errors = new HashSet<>();
        addErrors(errors);
        return errors;
    }
}