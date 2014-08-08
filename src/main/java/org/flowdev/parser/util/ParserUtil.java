package org.flowdev.parser.util;

import org.flowdev.base.data.Feedback;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.SourceData;

public abstract class ParserUtil {
    public static void fillResultMatched(ParserData parserData, int len) {
        ParseResult result = parserData.getResult();
        int srcPos = parserData.getSource().getPos();

        result.setErrPos(-1);
        result.setPos(srcPos);
        result.setText(parserData.getSource().getContent().substring(srcPos, srcPos + len));
        parserData.getSource().setPos(srcPos + len);
    }

    public static void fillResultUnmatched(ParserData parserData, int pos, String message) {
        Feedback feedback = new Feedback();
        feedback.getErrors().add(where(parserData.getSource(), parserData.getResult().getErrPos()) + message);

        fillResultUnmatchedAbsolut(parserData, parserData.getSource().getPos() + pos, feedback);
    }

    public static void fillResultUnmatchedAbsolut(ParserData parserData, int errPos, Feedback feedback) {
        ParseResult result = parserData.getResult();
        result.setErrPos(errPos);
        result.setPos(parserData.getSource().getPos());
        result.setText(null);
        result.setFeedback(feedback);
    }

    public static String where(SourceData source, int pos) {
        if (pos >= source.getWherePrevNl()) {
            return whereForward(source, pos);
        } else if (pos <= source.getWherePrevNl() - pos) {
            source.setWhereLine(1);
            source.setWherePrevNl(-1);
            return whereForward(source, pos);
        } else {
            return whereBackward(source, pos);
        }
    }

    private static String whereForward(SourceData source, int pos) {
        String where = null;
        String text = source.getContent();
        int lineNum = source.getWhereLine();   // Line number
        int prevNl = source.getWherePrevNl();  // Line start (position of preceding newline)
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
        String text = source.getContent();
        int lineNum = source.getWhereLine();   // Line number
        int prevNl;  // Line start (position of preceding newline)
        int nextNl = source.getWherePrevNl();   // Position of next newline or end

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
            source.setWherePrevNl(prevNl);
            source.setWhereLine(lineNum);
            return generateWhereMessage(source.getName(), lineNum, pos - prevNl,
                    source.getContent().substring(prevNl + 1, nextNl));
        } else if (prevNl >= nextNl) {
            return "ERROR: Unable to find position " + pos + " in file: " + source.getName();
        }
        return null;
    }

    static String generateWhereMessage(String name, int line, int col, String srcLine) {
        return ("File " + name + ", line " + line + ", column " + col + ":\n" + srcLine + "\n");
    }

    public static boolean matched(ParseResult result) {
        return result.getErrPos() < 0;
    }
}
