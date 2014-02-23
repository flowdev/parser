package org.flowdev.parser.util;

import org.flowdev.base.data.Feedback;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.SourceData;

import java.util.ArrayList;

public abstract class ParserUtil {
    public static void fillResultMatched(ParserData parserData, int len) {
        ParseResult result = parserData.result;
        result.errPos = -1;
        result.pos = parserData.source.pos;
        result.text = parserData.source.content.substring(result.pos, result.pos + len);
        parserData.source.pos += len;
    }

    public static void fillResultUnmatched(ParserData parserData, int pos, String message) {
        ParseResult result = parserData.result;
        result.errPos = parserData.source.pos + pos;
        result.pos = parserData.source.pos;
        result.text = "";
        if (result.feedback == null) {
            result.feedback = new Feedback();
        }
        if (result.feedback.errors == null) {
            result.feedback.errors = new ArrayList<>();
        }
        result.feedback.errors.add(message);
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

    public static boolean matched(ParseResult result) {
        return result.errPos < 0;
    }
}
