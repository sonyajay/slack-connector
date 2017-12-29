package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.extension.slack.internal.error.SlackError.FILE_UPLOAD;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.error.FileUploadErrorProvider;
import org.mule.extension.slack.internal.metadata.FileUploadOutputResolver;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileOperations extends SlackOperations {

//    protected static final org.mule.runtime.api.metadata.MediaType MULTIPART = org.mule.runtime.api.metadata.MediaType.create("multipart", "form-data");

    /**
     * This operation allows you to create or upload an existing file.
     *
     * @param slackConnection The connection
     * @param content         File contents
     * @param channels        List of channel names or IDs where the file will be shared.
     * @param fileName        Filename of file.
     * @param title           Title of file.
     * @param initialComment  Initial comment to add to file.
     * @param fileType        A file type identifier. See: https://api.slack.com/types/file#file_types
     * @param callback
     */
    @OutputResolver(output = FileUploadOutputResolver.class)
    @MediaType(APPLICATION_JSON)
    @DisplayName("File - Upload")
    public void uploadFile(@Connection SlackConnection slackConnection,
                           @Content(primary = true) TypedValue<InputStream> content,
                           @Content @Example("[\n  \"C03NE28RY\" \n]") List<String> channels,
                           @Example("Sunset photo") String fileName,
                           @Optional @Example("An awesome photo!") String title,
                           @Optional @Example("Hi!, this is the photo I've talked about") String initialComment,
                           @Optional @Example("image/jpg") String fileType,
                           CompletionCallback<InputStream, Void> callback) throws IOException {

//        if(MULTIPART.matches(content.getDataType().getMediaType())){
//            InputStream inputStream = content.getValue();
//                TypedValue<?> payload = expressionManager.evaluate("#[payload.parts.file.content]", BindingContext.builder().addBinding("payload", content).build());
//                ((Cursor) inputStream).seek(0);
//                TypedValue<String> name = (TypedValue<String>) expressionManager.evaluate("#[output application/java --- payload.parts.file.headers.'Content-Disposition'.name]", BindingContext.builder().addBinding("payload", content).build());
//
//                if(fileName == null){
//                    fileName = name.getValue();
//                }
//                Object value = payload.getValue();
//            if(value instanceof CursorStreamProvider) {
//                content = new TypedValue<>(((CursorStreamProvider) value).openCursor(), payload.getDataType(), payload.getLength());
//            }
//        }

        slackConnection.file.upload(channels, content, fileName, fileType, initialComment, title)
                .whenCompleteAsync(new HttpResponseConsumer<>("#[payload.file]", EXECUTION, callback));
    }
}
