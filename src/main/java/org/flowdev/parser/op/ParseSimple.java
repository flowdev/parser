package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.matched;

public abstract class ParseSimple<T, C> extends BaseParser<T, C> {
    protected ParseSimple(Params<T> params) {
        super(params);
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.result = new ParseResult();
        C cfg = getVolatileConfig();

        parseSimple(parserData.source.content.substring(parserData.source.pos), cfg, parserData);

        params.setParserData.set(data, parserData);
        if (semOutPort != null && matched(parserData.result)) {
            semOutPort.send(data);
        } else {
            outPort.send(data);
        }
    }

    public abstract void parseSimple(String substring, C cfg, ParserData parserData);
}
