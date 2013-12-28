package org.flowdev.parser.util;

import org.flowdev.base.data.Feedback;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.SourceData;

import java.util.ArrayList;

public abstract class ParserUtil {
    public static void addError(ParserData parserData, int pos, String message) {
        String fullMessage = message + "\n" + where(parserData.source, pos);
        if (parserData.feedback == null) {
            parserData.feedback = new Feedback();
        }
        if (parserData.feedback.errors == null) {
            parserData.feedback.errors = new ArrayList<>();
        }
        parserData.feedback.errors.add(fullMessage);
    }

    public static String where(SourceData source, int pos) {
        String text = source.content;
        int lineNum = 1;   // Line number
        int lineStart = -1;  // Line start (position of preceding newline)
        int nextNl;   // Position of next newline or end

        while (true) {
            nextNl = text.indexOf('\n', lineStart + 1);
            if (nextNl < 0) {
                nextNl = text.length();
            }
            if (lineStart < pos && pos <= nextNl) {
                return ("File " + source.name + ", line " + lineNum + ", column " + (pos - lineStart) + ":\n" + //
                        text.substring(lineStart + 1, nextNl) + "\n");
            }
            lineStart = nextNl;
            lineNum++;
        }
    }
}
