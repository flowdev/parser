package org.flowdev.parser.op;

import org.flowdev.base.Getter;
import org.flowdev.base.Setter;
import org.flowdev.base.op.Filter;
import org.flowdev.parser.data.ParseLiteralConfig;
import org.flowdev.parser.data.ParserData;

/**
 * Created by obulbuk on 24.12.13.
 */
public class ParseLiteral<T> extends Filter<T, ParseLiteralConfig> {
    public static class Params<T> {
        public Getter<T, ParserData> getParserData;
        public Setter<ParserData, T, T> setParserData;
    }

    private Params<T> params;

    public ParseLiteral(Params<T> params) {
        this.params = params;
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        String literal = getVolatileConfig().literal;
        int orgPos = parserData.source.pos;
        if (literal.equals(parserData.source.content.substring(orgPos, orgPos + literal.length()))) {
            parserData.result.pos = orgPos;
            parserData.result.text = literal;
            parserData.result.matched = true;
            parserData.source.pos += parserData.result.text.length();
        } else {
            parserData.result.matched = false;
        }
        params.setParserData.set(data, parserData);
        outPort.send(data);
    }
}
