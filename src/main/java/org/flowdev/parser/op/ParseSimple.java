package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.matched;

public abstract class ParseSimple<T, C> extends BaseParser<T, C> {
    protected ParseSimple(ParserParams<T> params) {
        super(params);
    }

    @Override
    protected void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.setResult(new ParseResult());
        C cfg = getVolatileConfig();

        parseSimple(parserData.getSource().getContent().substring(parserData.getSource().getPos()), cfg, parserData);

        if (semOutPort != null && matched(parserData.getResult())) {
            semOutPort.send(params.setParserData.set(data, parserData));
        } else {
            if (matched(parserData.getResult())) {
                defaultSemantics(parserData);
            }
            outPort.send(params.setParserData.set(data, parserData));
        }
    }

    protected void defaultSemantics(ParserData data) {
        data.getResult().setValue(data.getResult().getText());
    }

    public abstract void parseSimple(String substring, C cfg, ParserData parserData);
}
