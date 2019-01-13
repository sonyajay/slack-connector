package org.mule.extension.slack.internal.source;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;

public class SlackEventReceiver extends Source<String, Void> {

    @Override
    public void onStart(SourceCallback<String, Void> sourceCallback) throws MuleException {

    }

    @Override
    public void onStop() {

    }
}
