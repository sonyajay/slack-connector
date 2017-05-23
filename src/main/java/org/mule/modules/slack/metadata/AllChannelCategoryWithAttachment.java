/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.slack.metadata;

import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.JSONMetaDataBuilder;
import org.mule.modules.slack.SlackConnector;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

@MetaDataCategory
public class AllChannelCategoryWithAttachment extends AllChannelCategory {

    @Inject
    private SlackConnector connector;

    @MetaDataRetriever
    public MetaData describeEntity(MetaDataKey entityKey) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        JSONMetaDataBuilder metaDataBuilder = builder.createJsonObject().setSchema(attachmentsSchema);

        return new DefaultMetaData(metaDataBuilder.build());
    }

//    @MetaDataOutputRetriever
//    public MetaData describeOutput(MetaDataKey entityKey) throws Exception {
//        De
//    }

    private String getJsonSchema() throws IOException {
        File attachmentsSchemaFile = new File(getClass().getClassLoader().getResource("schemas/attachments-schema.json").getFile());
        return IOUtils.toString(new FileInputStream(attachmentsSchemaFile));
    }

    public SlackConnector getConnector() {
        return connector;
    }

    public void setConnector(SlackConnector connector) {
        this.connector = connector;
    }

    private static final String attachmentsSchema = "{\n" +
            "  \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
            "  \"definitions\": {},\n" +
            "  \"id\": \"Attachments\",\n" +
            "  \"items\": {\n" +
            "    \"id\": \"/items\",\n" +
            "    \"properties\": {\n" +
            "      \"author_icon\": {\n" +
            "        \"id\": \"/items/properties/author_icon\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"author_link\": {\n" +
            "        \"id\": \"/items/properties/author_link\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"author_name\": {\n" +
            "        \"id\": \"/items/properties/author_name\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"color\": {\n" +
            "        \"id\": \"/items/properties/color\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"fallback\": {\n" +
            "        \"id\": \"/items/properties/fallback\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"fields\": {\n" +
            "        \"id\": \"/items/properties/fields\",\n" +
            "        \"items\": {\n" +
            "          \"id\": \"/items/properties/fields/items\",\n" +
            "          \"properties\": {\n" +
            "            \"short\": {\n" +
            "              \"id\": \"/items/properties/fields/items/properties/short\",\n" +
            "              \"type\": \"boolean\"\n" +
            "            },\n" +
            "            \"title\": {\n" +
            "              \"id\": \"/items/properties/fields/items/properties/title\",\n" +
            "              \"type\": \"string\"\n" +
            "            },\n" +
            "            \"value\": {\n" +
            "              \"id\": \"/items/properties/fields/items/properties/value\",\n" +
            "              \"type\": \"string\"\n" +
            "            }\n" +
            "          },\n" +
            "          \"type\": \"object\"\n" +
            "        },\n" +
            "        \"type\": \"array\"\n" +
            "      },\n" +
            "      \"footer\": {\n" +
            "        \"id\": \"/items/properties/footer\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"footer_icon\": {\n" +
            "        \"id\": \"/items/properties/footer_icon\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"image_url\": {\n" +
            "        \"id\": \"/items/properties/image_url\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"mrkdwn_in\": {\n" +
            "        \"id\": \"/items/properties/mrkdwn_in\",\n" +
            "        \"items\": {\n" +
            "          \"id\": \"/items/properties/mrkdwn_in/items\",\n" +
            "          \"type\": \"string\",\n" +
            "          \"enum\" : [\"text\",\"pretext\",\"fields\"]\n" +
            "        },\n" +
            "        \"type\": \"array\"\n" +
            "      },\n" +
            "      \"pretext\": {\n" +
            "        \"id\": \"/items/properties/pretext\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"text\": {\n" +
            "        \"id\": \"/items/properties/text\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"thumb_url\": {\n" +
            "        \"id\": \"/items/properties/thumb_url\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"title\": {\n" +
            "        \"id\": \"/items/properties/title\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"title_link\": {\n" +
            "        \"id\": \"/items/properties/title_link\",\n" +
            "        \"type\": \"string\"\n" +
            "      },\n" +
            "      \"ts\": {\n" +
            "        \"id\": \"/items/properties/ts\",\n" +
            "        \"type\": \"integer\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"type\": \"object\"\n" +
            "  },\n" +
            "  \"type\": \"array\"\n" +
            "}";

}