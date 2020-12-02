package org.mule.extension.slack.internal.operations.reactions;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;

public class FileReactionGroup {

    @Parameter
    @Example("F1234567890") String file;

    @Parameter
    @Example("Fc1234567890")
    String fileComment;

    public String getFile() {
        return file;
    }

    public String getFileComment() {
        return fileComment;
    }
}
