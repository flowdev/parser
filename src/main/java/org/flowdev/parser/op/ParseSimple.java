package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Setter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

public abstract class ParseSimple<T, C> extends Filter<T, C> {
    public static class Params<T> {
        public Getter<T, ParserData> getParserData;
        public Setter<ParserData, T, T> setParserData;
    }

    protected Params<T> params;

    protected ParseSimple(Params<T> params) {
        this.params = params;
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.result = new ParseResult();
        C cfg = getVolatileConfig();
        int orgPos = parserData.source.pos;
        int newPos = orgPos;
        if (parserData.source.content.length() > orgPos) {
            newPos += parseSimple(parserData.source.content.substring(orgPos), cfg, parserData);
        }
        if (orgPos != newPos) {
            parserData.result.pos = orgPos;
            parserData.result.text = parserData.source.content.substring(orgPos, newPos);
            parserData.result.matched = true;
            parserData.source.pos = newPos;
        } else {
            parserData.result.matched = false;
        }
        params.setParserData.set(data, parserData);
        outPort.send(data);
    }

    public abstract int parseSimple(String substring, C cfg, ParserData parserData);
}
