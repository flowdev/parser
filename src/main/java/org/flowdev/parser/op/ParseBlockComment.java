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
        if (!substring.startsWith(cfg.commentStart())) {
            fillResultUnmatched(parserData, 0, "Block comment expected.");
            return;
        }

        int pos = cfg.commentStart().length();
        int level = 1;
        int iEnd = substring.indexOf(cfg.commentEnd(), pos);
        int iBeg = substring.indexOf(cfg.commentStart(), pos);
        while (level > 0) {
            if (iEnd < 0) {
                fillResultUnmatched(parserData, pos, "Block comment isn't closed properly.");
                parserData.source().pos(parserData.source().pos() + pos);
                return;
            }
            if (iBeg >= 0 && iBeg < iEnd) {
                level++;
                pos = iBeg + cfg.commentStart().length();
                iBeg = substring.indexOf(cfg.commentStart(), pos);
            } else {
                level--;
                pos = iEnd + cfg.commentEnd().length();
                iEnd = substring.indexOf(cfg.commentEnd(), pos);
            }
        }
        fillResultMatched(parserData, pos);
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        data.result().value("");
    }

    public static class ParseBlockCommentConfig {
        private String commentStart;
        private String commentEnd;

        public ParseBlockCommentConfig commentStart(final String commentStart) {
            this.commentStart = commentStart;
            return this;
        }

        public ParseBlockCommentConfig commentEnd(final String commentEnd) {
            this.commentEnd = commentEnd;
            return this;
        }

        public String commentStart() {
            return commentStart;
        }

        public String commentEnd() {
            return commentEnd;
        }
    }
}
