package org.mule.extension.slack.internal.operations;

import static org.mule.extension.slack.internal.error.SlackError.EXECUTION;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.extension.slack.internal.metadata.FileUploadOutputResolver;
import org.mule.extension.slack.internal.metadata.ListFilesOutputResolver;
import org.mule.extension.slack.internal.utils.CursorPagingProvider;
import org.mule.extension.slack.internal.utils.PagedPagingProvider;
import org.mule.extension.slack.internal.valueprovider.ChannelsValueProvider;
import org.mule.extension.slack.internal.valueprovider.UsersValueProvider;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import scala.Int;

public class FileOperations extends SlackOperations {

    public static final Result<Void, Void> VOID_RESULT = Result.<Void, Void>builder().build();

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
                           @Optional @Example("Hi!, this is the photo I've talked about") @Text String initialComment,
                           @Optional @Example("image/jpg") String fileType,
                           @Optional @Example("1234567890.123456") String threadTimeStamp,
                           CompletionCallback<InputStream, Void> callback) {
        slackConnection.file.upload(channels, content, fileName, fileType, initialComment, title, threadTimeStamp)
                .whenCompleteAsync(createConsumer("#[payload.file]", EXECUTION, callback));
    }

    /**
     * This operation deletes a file from your team.
     *
     * @param slackConnection The connection
     * @param file            ID of file to delete.
     * @param callback
     */
    @DisplayName("File - Delete")
    public void deleteFile(@Connection SlackConnection slackConnection,
                           @DisplayName("File ID") @Example("F1234567890") String file,
                           CompletionCallback<Void, Void> callback) {
        slackConnection.file.delete(file)
                .whenCompleteAsync(createConsumer("#[payload]", EXECUTION, new CompletionCallback() {
                    @Override
                    public void success(Result result) {
                        callback.success(VOID_RESULT);
                    }

                    @Override
                    public void error(Throwable throwable) {
                        callback.error(throwable);
                    }
                }));
    }

    /**
     * This operations returns a list of files within the team. It can be filtered and sliced in various ways.
     *
     * @param channel Filter files appearing in a specific channel, indicated by its ID
     * @param user
     * @param tsFrom  Filter files created after this timestamp (inclusive).
     * @param tsTo    Filter files created before this timestamp (inclusive).
     * @param type    Filter files by type. You can pass multiple values in the types argument, like "spaces,snippets". The default value is all, which does not filter the list.
     */
    @OutputResolver(output = ListFilesOutputResolver.class)
    @MediaType("application/java")
    @DisplayName("File - List")
    public PagingProvider<SlackConnection, Map<String, Object>> listFiles(@Optional @OfValues(ChannelsValueProvider.class) @Example("C03NE28RY") String channel,
                                                                          @Optional @OfValues(UsersValueProvider.class) @Example("W1234567890") String user,
                                                                          @Optional @Example("now() as Number {unit: \"milliseconds\"}") @DisplayName("Timestamp From") String tsFrom,
                                                                          @Optional @DisplayName("Timestamp To") String tsTo,
                                                                          @Optional @OfValues(FileTypesValueProvider.class) String type) {
        return new PagedPagingProvider((connection, pageNumber) -> connection.file.list(pageNumber, channel, tsFrom, tsTo, type, user, 100), "#[output application/java --- payload.files]", this.expressionManager);

    }

}
