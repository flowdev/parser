package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.ArrayList;


public class ParseAlternatives<T> extends ParseWithMultipleSubOp<T, EmptyConfig> {
    public ParseAlternatives(Params<T> params) {
        super(params);
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.tempStack.add(new ParserTempData(parserData.source.pos));
        parserData.subResults = new ArrayList<>(1);
        subOutPorts.get(0).send(data);
    }

    @Override
    protected void handleSubOpData(T data) {
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.tempStack.get(0);
        long count = tempData.index;
        if (parserData.result.matched) {
            semOutPort.send(params.setParserData.set(data, parserData));
        } else {
            count++;
            if (count >= subOutPorts.size()) {
                parserData.result = createUnmatchedResult(tempData.orgSrcPos);
                parserData.source.pos = tempData.orgSrcPos;
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.result = null;
                subOutPorts.get((int) count).send(params.setParserData.set(data, parserData));
            }
        }
    }

    private static ParseResult createUnmatchedResult(int pos) {
        ParseResult result = new ParseResult();
        result.matched = false;
        result.pos = pos;
        result.text = "";
        result.value = null;
        return result;
    }

    @Override
    public int parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
