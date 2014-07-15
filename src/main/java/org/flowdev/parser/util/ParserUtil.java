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
        result.text = null;
        if (result.feedback == null) {
            result.feedback = new Feedback();
        }
        if (result.feedback.getErrors() == null) {
            result.feedback.setErrors(new ArrayList<>());
        }
        result.feedback.getErrors().add(message);
    }

    public static String where(SourceData source, int pos) {
        if (pos >= source.wherePrevNl) {
            return whereForward(source, pos);
        } else if (pos <= source.wherePrevNl - pos) {
            source.whereLine = 1;
            source.wherePrevNl = -1;
            return whereForward(source, pos);
        } else {
            return whereBackward(source, pos);
        }
    }

    private static String whereForward(SourceData source, int pos) {
        String where = null;
        String text = source.content;
        int lineNum = source.whereLine;   // Line number
        int prevNl = source.wherePrevNl;  // Line start (position of preceding newline)
        int nextNl;   // Position of next newline or end

        while (where == null) {
            nextNl = text.indexOf('\n', prevNl + 1);
            if (nextNl < 0) {
                nextNl = text.length();
            }
            where = tryWhere(source, prevNl, pos, nextNl, lineNum);
            prevNl = nextNl;
            lineNum++;
        }
        return where;
    }

    private static String whereBackward(SourceData source, int pos) {
        String where = null;
        String text = source.content;
        int lineNum = source.whereLine;   // Line number
        int prevNl;  // Line start (position of preceding newline)
        int nextNl = source.wherePrevNl;   // Position of next newline or end

        while (where == null) {
            prevNl = text.lastIndexOf('\n', nextNl - 1);
            where = tryWhere(source, prevNl, pos, nextNl, lineNum);
            nextNl = prevNl;
            lineNum--;
        }
        return where;
    }

    private static String tryWhere(SourceData source, int prevNl, int pos, int nextNl, int lineNum) {
        if (prevNl < pos && pos <= nextNl) {
            source.wherePrevNl = prevNl;
            source.whereLine = lineNum;
            return generateWhereMessage(source.name, lineNum, pos - prevNl,
                    source.content.substring(prevNl + 1, nextNl));
        } else if (prevNl >= nextNl) {
            return "ERROR: Unable to find position " + pos + " in file: " + source.name;
        }
        return null;
    }

    static String generateWhereMessage(String name, int line, int col, String srcLine) {
        return ("File " + name + ", line " + line + ", column " + col + ":\n" + srcLine + "\n");
    }

    public static boolean matched(ParseResult result) {
        return result.errPos < 0;
    }
}
