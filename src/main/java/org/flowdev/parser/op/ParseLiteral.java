package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseLiteralConfig;
import org.flowdev.parser.data.ParserData;


public class ParseLiteral<T> extends ParseSimple<T, ParseLiteralConfig> {
    public ParseLiteral(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, ParseLiteralConfig cfg, ParserData parserData) {
        return substring.startsWith(cfg.literal) ? cfg.literal.length() : 0;
    }
}
