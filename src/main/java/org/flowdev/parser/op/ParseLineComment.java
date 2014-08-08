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
        if (!substring.startsWith(cfg.getCommentStart())) {
            fillResultUnmatched(parserData, 0, "Line comment starting with '" + cfg.getCommentStart() + "' expected.");
            return;
        }

        int idx = substring.indexOf('\n');
        fillResultMatched(parserData, idx >= 0 ? idx : substring.length());
    }

    @Override
    protected void defaultSemantics(ParserData data) {
        data.getResult().setValue("");
    }

    public static class ParseLineCommentConfig {
        private final String commentStart;

        public ParseLineCommentConfig(String commentStart) {
            this.commentStart = commentStart;
        }

        public String getCommentStart() {
            return commentStart;
        }
    }
}
