package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;

public class ParseBlockComment<T> extends ParseSimple<T, ParseBlockComment.ParseBlockCommentConfig> {
    public ParseBlockComment(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseBlockCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.getCommentStart())) {
            fillResultUnmatched(parserData, 0, "Block comment expected.");
            return;
        }

        int pos = cfg.getCommentStart().length();
        int level = 1;
        int iEnd = substring.indexOf(cfg.getCommentEnd(), pos);
        int iBeg = substring.indexOf(cfg.getCommentStart(), pos);
        while (level > 0) {
            if (iEnd < 0) {
                fillResultUnmatched(parserData, pos, "Block comment isn't closed properly.");
                parserData.getSource().setPos(parserData.getSource().getPos() + pos);
                return;
            }
            if (iBeg >= 0 && iBeg < iEnd) {
                level++;
                pos = iBeg + cfg.getCommentStart().length();
                iBeg = substring.indexOf(cfg.getCommentStart(), pos);
            } else {
                level--;
                pos = iEnd + cfg.getCommentEnd().length();
                iEnd = substring.indexOf(cfg.getCommentEnd(), pos);
            }
        }
        fillResultMatched(parserData, pos);
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        data.getResult().setValue("");
    }

    public static class ParseBlockCommentConfig {
        private final String commentStart;
        private final String commentEnd;

        public ParseBlockCommentConfig(String commentStart, String commentEnd) {
            this.commentStart = commentStart;
            this.commentEnd = commentEnd;
        }

        public String getCommentStart() {
            return commentStart;
        }

        public String getCommentEnd() {
            return commentEnd;
        }
    }
}
