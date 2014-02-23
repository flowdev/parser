package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseLineCommentConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseLineComment<T> extends ParseSimple<T, ParseLineCommentConfig> {
    public ParseLineComment(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseLineCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.commentStart)) {
            fillResultUnmatched(parserData, 0, "Line comment stating with '" + cfg.commentStart + "' expected.");
            return;
        }

        int idx = substring.indexOf('\n');
        fillResultMatched(parserData, idx >= 0 ? idx : substring.length());
    }
}
