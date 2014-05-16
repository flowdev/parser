package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseBlockCommentConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;

public class ParseBlockComment<T> extends ParseSimple<T, ParseBlockCommentConfig> {
    public ParseBlockComment(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseBlockCommentConfig cfg, ParserData parserData) {
        if (!substring.startsWith(cfg.commentStart)) {
            fillResultUnmatched(parserData, 0, "Block comment expected.");
            return;
        }

        int pos = cfg.commentStart.length();
        int level = 1;
        int iEnd = substring.indexOf(cfg.commentEnd, pos);
        int iBeg = substring.indexOf(cfg.commentStart, pos);
        while (level > 0) {
            if (iEnd < 0) {
                fillResultUnmatched(parserData, pos, "Block comment isn't closed properly.");
                parserData.source.pos += pos;
                return;
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
        fillResultMatched(parserData, pos);
    }
}
