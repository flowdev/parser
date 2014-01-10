package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParserData;


public class ParseOptional<T> extends ParseWithSingleSubOp<T, EmptyConfig> {
    public ParseOptional(Params<T> params) {
        super(params);
    }

    @Override
    public int parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        return 0;
    }
}
