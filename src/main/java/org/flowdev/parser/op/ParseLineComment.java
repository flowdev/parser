package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseLineCommentConfig;
import org.flowdev.parser.data.ParserData;


public class ParseLineComment<T> extends ParseSimple<T, ParseLineCommentConfig> {
    public ParseLineComment(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseLineCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.commentStart)) {
            return 0;
        }

        int idx = substring.indexOf('\n');
        if (idx >= 0) {
            return idx;
        } else {
            return substring.length();
        }
    }
}
