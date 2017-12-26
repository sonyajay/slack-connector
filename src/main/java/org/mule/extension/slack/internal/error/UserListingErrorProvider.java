package org.mule.extension.slack.internal.error;

import static org.mule.extension.slack.internal.error.SlackError.USER_LISTING;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

public class UserListingErrorProvider extends BaseErrorTypeProvider {

    @Override
    public void addErrors(Set<ErrorTypeDefinition> errors) {
        errors.add(USER_LISTING);
    }
}
