package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseLineComment<T> extends ParseSimple<T, ParseLineComment.ParseLineCommentConfig> {
    public ParseLineComment(ParserParams<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseLineCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.commentStart())) {
            fillResultUnmatched(parserData, 0, "Line comment starting with '" + cfg.commentStart() + "' expected.");
            return;
        }

        int idx = substring.indexOf('\n');
        fillResultMatched(parserData, idx >= 0 ? idx : substring.length());
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        data.result().value("");
    }

    public static class ParseLineCommentConfig {
        private String commentStart;

        public ParseLineCommentConfig commentStart(final String commentStart) {
            this.commentStart = commentStart;
            return this;
        }

        public String commentStart() {
            return commentStart;
        }
    }
}
