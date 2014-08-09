package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

public abstract class ParseSimple<T, C> extends BaseParser<T, C> {
    protected ParseSimple(ParserParams<T> params) {
        super(params);
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.result(new ParseResult());
        C cfg = getVolatileConfig();

        parseSimple(parserData.source().content().substring(parserData.source().pos()), cfg, parserData);

        outPort.send(handleSemantics(data));
    }

    public abstract void parseSimple(String substring, C cfg, ParserData parserData);
}
