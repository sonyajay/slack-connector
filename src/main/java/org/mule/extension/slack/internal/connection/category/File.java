package org.mule.extension.slack.internal.connection.category;

import static java.lang.String.join;
import static java.util.Collections.singleton;
import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.FILES_DELETE;
import static org.mule.extension.slack.internal.connection.SlackMethods.FILES_UPLOAD;
import static org.mule.runtime.http.api.HttpConstants.Method.GET;
import static org.mule.runtime.http.api.HttpConstants.Method.POST;

import org.mule.extension.slack.internal.connection.SlackConnection;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.http.api.domain.entity.multipart.HttpPart;
import org.mule.runtime.http.api.domain.entity.multipart.MultipartHttpEntity;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class File {

    private SlackConnection slackConnection;

    public File(SlackConnection slackConnection) {
        this.slackConnection = slackConnection;
    }

    public CompletableFuture<HttpResponse> upload(List<String> channels, TypedValue<InputStream> content, String filename, String fileType, String initialComment, String title, String threadTimeStamp) {
        MultiMap<String, String> parameterMap = new MultiMap<>();

        parameterMap.put("channels", join(",", channels));
        ifPresent(initialComment, value -> parameterMap.put("initial_comment", value));
        ifPresent(title, value -> parameterMap.put("title", value));
        ifPresent(filename, value -> parameterMap.put("fileType", value));
        ifPresent(threadTimeStamp, value -> parameterMap.put("thread_ts", value));

        byte[] byteArrayContent = IOUtils.toByteArray(content.getValue());
        MultipartHttpEntity httpEntity = new MultipartHttpEntity(singleton(new HttpPart("file", filename, byteArrayContent, content.getDataType().getMediaType().toString(), byteArrayContent.length)));
        return slackConnection.sendAsyncRequest(POST, API_URI + FILES_UPLOAD, parameterMap, httpEntity);
    }

    public CompletableFuture<HttpResponse> delete(String fileId) {
        MultiMap<String, String> parameterMap = new MultiMap<>();

        parameterMap.put("file", fileId);
        return slackConnection.sendAsyncRequest(API_URI + FILES_DELETE, parameterMap);
    }

}
