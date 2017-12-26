package org.mule.extension.slack.internal.utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class JsonListInputStream extends InputStream {

    private Iterator<InputStream> streamIterator;
    private InputStream currentList;
    private int currentJson = 0;
    private int currentPos;
    private boolean inComment;
    private int arrayLevel;
    private boolean end = false;

    public JsonListInputStream(String... lists) {
        streamIterator = of(lists)
                .map(s -> (InputStream) new ByteArrayInputStream(s.getBytes()))
                .collect(toList())
                .iterator();
    }

    public JsonListInputStream(Iterator<InputStream> streamIterator) {
        this.streamIterator = streamIterator;
    }

    public int read() throws IOException {
        if (end) {
            return -1;
        }

        if (currentList == null) {
            currentList = streamIterator.next();
        }

        char currentChar = (char) currentList.read();

        if (currentChar == '"') {
            inComment = !inComment;
        }

        if (currentChar == '[' && !inComment) {
            arrayLevel++;
            if (currentJson > 0) {
                currentPos++;
                currentChar = (char) currentList.read();
            }
        }

        if (currentChar == ']' && !inComment) {
            arrayLevel--;
            if (arrayLevel == 0) {
                if (!streamIterator.hasNext()) {
                    end = true;
                    return ']';
                }
                currentJson++;
                currentList = streamIterator.next();
                currentPos = 0;
                return ',';
            }
        }

        currentPos++;
        return currentChar;
    }
}