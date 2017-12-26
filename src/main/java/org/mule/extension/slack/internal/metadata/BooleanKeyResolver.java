package org.mule.extension.slack.internal.metadata;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeyBuilder;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BooleanKeyResolver implements TypeKeysResolver {

    private static final Set<MetadataKey> keys = new HashSet<MetadataKey>() {
        {
            this.add(MetadataKeyBuilder.newKey("TRUE").build());
            this.add(MetadataKeyBuilder.newKey("FALSE").build());
        }
    };

    public BooleanKeyResolver() {
    }

    public String getCategoryName() {
        return "open-im";
    }

    public Set<MetadataKey> getKeys(MetadataContext context) throws MetadataResolvingException, ConnectionException {
        return Collections.unmodifiableSet(keys);
    }
}
