package org.mule.extension.slack.internal;

import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyPart;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.StringJoiner;

public class ConversationTypes {

    @Parameter
    @MetadataKeyPart(order = 1)
    @Optional(defaultValue = "true")
    private boolean publicChannels;

    @Parameter
    @MetadataKeyPart(order = 2)
    @Optional(defaultValue = "false")
    private boolean privateChannels;

    @Parameter
    @MetadataKeyPart(order = 3)
    @Optional(defaultValue = "false")
    private boolean im;

    @Parameter
    @MetadataKeyPart(order = 4)
    @Optional(defaultValue = "false")
    private boolean mpim;

    public boolean isPublicChannels() {
        return publicChannels;
    }

    public void setPublicChannels(boolean publicChannels) {
        this.publicChannels = publicChannels;
    }

    public boolean isPrivateChannels() {
        return privateChannels;
    }

    public void setPrivateChannels(boolean privateChannels) {
        this.privateChannels = privateChannels;
    }

    public boolean isIm() {
        return im;
    }

    public void setIm(boolean im) {
        this.im = im;
    }

    public boolean isMpim() {
        return mpim;
    }

    public void setMpim(boolean mpim) {
        this.mpim = mpim;
    }

    public String getRequiredTypes() {
        StringJoiner stringJoiner = new StringJoiner(",");

        if (publicChannels) {
            stringJoiner.add("public_channel");
        }

        if (privateChannels) {
            stringJoiner.add("private_channel");
        }

        if (im) {
            stringJoiner.add("im");
        }

        if (mpim) {
            stringJoiner.add("mpim");
        }

        return stringJoiner.toString();
    }
}
