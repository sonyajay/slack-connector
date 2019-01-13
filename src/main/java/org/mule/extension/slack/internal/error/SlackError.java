package org.mule.extension.slack.internal.error;

import static java.util.Optional.ofNullable;
import static org.mule.runtime.extension.api.error.MuleErrors.CONNECTIVITY;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Optional;

/**
 * mule-alltogether
 *
 * @author Esteban Wasinger (http://github.com/estebanwasinger)
 */
public enum SlackError implements ErrorTypeDefinition<SlackError> {

    EXECUTION,
    PUBLISHING(EXECUTION),
    DESCRIBING(EXECUTION),
    CHANNEL_LISTING(EXECUTION),
    USER_LISTING(EXECUTION),
    FILE_UPLOAD(EXECUTION),
    TRIGGER_EXPIRED(EXECUTION),
    NOT_AUTHED(CONNECTIVITY),
    INVALID_AUTH(CONNECTIVITY),
    NO_PERMISSION(CONNECTIVITY),
    ACCOUNT_INACTIVE(CONNECTIVITY);

    private ErrorTypeDefinition parent;

    SlackError(ErrorTypeDefinition parent) {
        this.parent = parent;
    }

    SlackError() {

    }

    @Override
    public Optional<ErrorTypeDefinition<? extends Enum<?>>> getParent() {
        return ofNullable(parent);
    }
}
