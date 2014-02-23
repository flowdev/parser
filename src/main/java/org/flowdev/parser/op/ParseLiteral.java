package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseLiteralConfig;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.fillResultMatched;
import static org.flowdev.parser.util.ParserUtil.fillResultUnmatched;


public class ParseLiteral<T> extends ParseSimple<T, ParseLiteralConfig> {
    public ParseLiteral(Params<T> params) {
        super(params);
    }

    @Override
    public void parseSimple(String substring, ParseLiteralConfig cfg, ParserData parserData) {
        if (substring.startsWith(cfg.literal)) {
            fillResultMatched(parserData, cfg.literal.length());
        } else {
            fillResultUnmatched(parserData, 0, "Literal '" + cfg.literal + "' expected.");
        }
    }
}
