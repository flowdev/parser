package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import static org.flowdev.parser.util.ParserUtil.matched;


public class ParseAlternatives<T> extends ParseWithMultipleSubOp<T, EmptyConfig> {
    public ParseAlternatives(Params<T> params) {
        super(params);
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.tempStack.add(new ParserTempData(parserData.source.pos));
        subOutPorts.get(0).send(data);
    }

    @Override
    protected void handleSubOpData(T data) {
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.tempStack.get(parserData.tempStack.size() - 1);
        if (matched(parserData.result)) {
            parserData.tempStack.remove(parserData.tempStack.size() - 1);
            semOutPort.send(params.setParserData.set(data, parserData));
        } else {
            tempData.index++;
            if (tempData.index >= subOutPorts.size()) {
                // FIXME: use ParserUtil.fillResultUnmatched
                parserData.result = createUnmatchedResult(tempData.orgSrcPos);
                parserData.source.pos = tempData.orgSrcPos;
                parserData.tempStack.remove(parserData.tempStack.size() - 1);
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.result = null;
                subOutPorts.get(tempData.index).send(params.setParserData.set(data, parserData));
            }
        }
    }

    private static ParseResult createUnmatchedResult(int pos) {
        ParseResult result = new ParseResult();
        result.errPos = pos;
        result.pos = pos;
        result.text = "";
        result.value = null;
        return result;
    }

    @Override
    public void parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
