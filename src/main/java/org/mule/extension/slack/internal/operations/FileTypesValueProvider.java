package org.mule.extension.slack.internal.operations;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Set;

public class FileTypesValueProvider implements ValueProvider {
    @Override
    public Set<Value> resolve() throws ValueResolvingException {
        return ValueBuilder.getValuesFor("all", "spaces", "snippets", "images", "gdocs", "zips", "pdfs");
    }
}
