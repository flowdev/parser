package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseBlockCommentConfig;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.util.ParserUtil;

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
        int iBeg = substring.indexOf(cfg.commentStart, pos);
        while (level > 0) {
            if (iEnd < 0) {
                ParserUtil.addError(parserData, parserData.source.pos + iBeg, "Block comment isn't closed properly.");
                parserData.source.pos += cfg.commentStart.length();
                return 0;
            }
            if (iBeg >= 0 && iBeg < iEnd) {
                level++;
                pos = iBeg + cfg.commentStart.length();
                iBeg = substring.indexOf(cfg.commentStart, pos);
            } else {
                level--;
                pos = iEnd + cfg.commentEnd.length();
                iEnd = substring.indexOf(cfg.commentEnd, pos);
            }
        }
        return pos;
    }
}
