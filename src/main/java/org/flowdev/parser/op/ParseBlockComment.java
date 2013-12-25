package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseBlockCommentConfig;
import org.flowdev.parser.data.ParserData;

public class ParseBlockComment<T> extends ParseSimple<T, ParseBlockCommentConfig> {
    public ParseBlockComment(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseBlockCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.commentStart)) {
            return 0;
        }

        int pos = cfg.commentStart.length();
        int level = 1;
        int iEnd = substring.indexOf(cfg.commentEnd, pos);
        int iBeg2 = substring.indexOf(cfg.commentStart, pos);
        while (level > 0) {
            if (iEnd < 0) {
                parserData.feedback.errors.add("Block comment wasn't properly ended.");
                return 0;
            }
            if (iBeg2 >= 0 && iBeg2 < iEnd) {
                level++;
                pos = iBeg2 + cfg.commentStart.length();
                iBeg2 = substring.indexOf(cfg.commentStart, pos);
            } else {
                level--;
                pos = iEnd + cfg.commentEnd.length();
                iEnd = substring.indexOf(cfg.commentEnd);
            }
        }
        return pos;
    }
}
