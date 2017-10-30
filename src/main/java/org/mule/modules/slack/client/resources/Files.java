package org.mule.modules.slack.client.resources;

import static org.mule.modules.slack.client.Operations.FILES_UPLOAD;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.mule.modules.slack.client.Operations;
import org.mule.modules.slack.client.SlackRequester;
import org.mule.modules.slack.client.model.file.FileUploadResponse;

import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Files {

    private final SlackRequester slackRequester;
    private final Gson gson;

    public Files(SlackRequester slackRequester, Gson gson) {

        this.slackRequester = slackRequester;
        this.gson = gson;
    }

    // TODO -- Delete duplicated code
    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, InputStream file) {
        String output = slackRequester.newRequest(FILES_UPLOAD)
                .withParam("channels", channelId)
                .withParam("filename", fileName)
                .withParam("filetype", fileType)
                .withParam("title", title)
                .withParam("initial_comment", initialComment)
                .withBodyPart("file", file)
                .build().execute();

        return gson.fromJson(new JSONObject(output).getJSONObject("file").toString(), FileUploadResponse.class);
    }

    public FileUploadResponse sendFile(String channelId, String fileName, String fileType, String title, String initialComment, String filePath) throws IOException {
        String output = slackRequester.newRequest(FILES_UPLOAD)
                .withParam("channels", channelId)
                .withParam("filename", fileName)
                .withParam("filetype", fileType)
                .withParam("title", title)
                .withParam("initial_comment", initialComment)
                .withBodyPart("file", new FileInputStream(filePath))
                .build().execute();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File " + file.getAbsolutePath() + " does not exist!");
        }

        return gson.fromJson(new JSONObject(output).getJSONObject("file").toString(), FileUploadResponse.class);
    }

}
