package org.mule.extension.slack.internal.metadata;

import static org.mule.metadata.json.api.JsonTypeLoader.JSON;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.InputStaticTypeResolver;

public class DialogInputTypeResolver extends InputStaticTypeResolver {

    @Override
    public MetadataType getStaticMetadata() {
        ObjectTypeBuilder dialogBuilder = BaseTypeBuilder.create(JSON).objectType();

        dialogBuilder.addField()
                .key("callback_id")
                .required()
                .value().stringType();

        dialogBuilder.addField()
                .key("title")
                .required()
                .value().stringType();


        dialogBuilder.addField()
                .key("submit_label")
                .required()
                .value().stringType();

        dialogBuilder.addField()
                .key("elements")
                .required()
                .value().arrayType()
                .of(getElementType());

        return dialogBuilder.build();
    }

        private MetadataType getElementType() {
            ObjectTypeBuilder elementBuilder = BaseTypeBuilder.create(JSON).objectType();

        elementBuilder
                .addField()
                .key("label")
                .description("Label displayed to user. Required. 24 character maximum.")
                .required().value().stringType();

        elementBuilder.addField()
                .key("name")
                .description("Name of form element. Required. No more than 300 characters.")
                .required()
                .value().stringType();

        elementBuilder.addField()
                .key("type")
                .description("The type of form element. For a text input, the type is always text. Required.")
                .required()
                .value().stringType()
                .enumOf("text", "textarea", "select");

        elementBuilder.addField()
                .key("subtype")
                .description("A subtype for this text input. Accepts email, number, tel, or url. In some form factors, optimized input is provided for this subtype.")
                .value().stringType().enumOf("email", "number", "tel", "url");

        elementBuilder.addField()
                .key("max_length")
                .description("Maximum input length allowed for element. Up to 150 characters. Defaults to 150.")
                .value().numberType()
                .defaultValue("150").integer();

        elementBuilder.addField()
                .key("min_length")
                .description("Minimum input length allowed for element. Up to 150 characters. Defaults to 0.")
                .value().numberType()
                .defaultValue("0").integer();

        elementBuilder.addField()
                .key("optional")
                .description("Provide true when the form element is not required. By default, form elements are required.")
                .value().booleanType()
                .defaultValue("true");

        elementBuilder.addField()
                .key("hint")
                .description("Helpful text provided to assist users in answering a question. Up to 150 characters.")
                .value().stringType();

        elementBuilder.addField()
                .key("value")
                .description("A default value for this field. Up to 500 characters.")
                .value().stringType();

        elementBuilder.addField()
                .key("placeholder")
                .description("A string displayed as needed to help guide users in completing the element. 150 character maximum.")
                .value().stringType();

        elementBuilder.addField()
                .key("options")
                .description("Provide up to 100 option element attributes. Required for this type.")
                .value()
                .arrayType()
                .of(createOptionType());

        return elementBuilder.build();
    }

    private MetadataType createOptionType() {
        ObjectTypeBuilder optionType = BaseTypeBuilder.create(JSON).objectType();

        optionType.addField().key("label").required()
                .value().stringType();

        optionType.addField().key("value").required()
                .value().stringType();

        return optionType.build();
    }

    @Override
    public String getCategoryName() {
        return "dialog";
    }

}
