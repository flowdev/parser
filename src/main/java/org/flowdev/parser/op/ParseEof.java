package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParserData;


public class ParseEof<T> extends ParseSimple<T, EmptyConfig> {
    public ParseEof(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        parserData.result.matched = substring.length() == 0;
        parserData.result.pos = parserData.source.pos;
        return 0;
    }
}
