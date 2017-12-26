package org.mule.extension.slack.internal.connection.category;

import static java.util.Collections.singleton;
import static org.mule.extension.slack.internal.connection.SlackConnection.ifPresent;
import static org.mule.extension.slack.internal.connection.SlackMethods.API_URI;
import static org.mule.extension.slack.internal.connection.SlackMethods.FILES_UPLOAD;
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

    public CompletableFuture<HttpResponse> upload(List<String> channels, TypedValue<InputStream> content, String filename, String filetype, String initialComment, String title) {
        MultiMap<String, String> parameterMap = new MultiMap<>();

        parameterMap.put("channels", channels.stream().collect(Collectors.joining(",")));
        ifPresent(initialComment, value -> parameterMap.put("initial_comment", value));
        ifPresent(title, value -> parameterMap.put("title", value));

        byte[] byteArrayContent = IOUtils.toByteArray(content.getValue());
        MultipartHttpEntity httpEntity = new MultipartHttpEntity(singleton(new HttpPart("file", filename, byteArrayContent, content.getDataType().getMediaType().toString(), byteArrayContent.length)));
        return slackConnection.sendAsyncRequest(POST, API_URI + FILES_UPLOAD, parameterMap, httpEntity);
    }

}
