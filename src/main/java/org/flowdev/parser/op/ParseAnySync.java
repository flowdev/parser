package org.flowdev.parser.op;

import org.flowdev.parser.data.ParserData;

import static org.flowdev.parser.util.ParserUtil.matched;

public abstract class ParseAnySync<T, C> extends BaseParser<T, C> {
    protected T dataFromSemantics;

    protected ParseAnySync(ParserParams<T> params) {
        super(params);
        semInPort = (data) -> dataFromSemantics = data;
    }

    @Override
    protected void filter(T data) {
        C cfg = getVolatileConfig();

        data = parseAnySync(data, cfg);

        ParserData parserData = params.getParserData.get(data);
        boolean matched = matched(parserData.getResult());
        if (matched && semOutPort != null) {
            semOutPort.send(data);
            data = dataFromSemantics;
        } else if (matched) {
            defaultSemantics(parserData);
            data = params.setParserData.set(data, parserData);
        }
        parserData = params.getParserData.get(data);
        parserData.setSubResults(null);
        outPort.send(params.setParserData.set(data, parserData));
    }

    protected void defaultSemantics(ParserData data) {
        data.getResult().setValue(data.getResult().getText());
    }

    public abstract T parseAnySync(T data, C cfg);
}
