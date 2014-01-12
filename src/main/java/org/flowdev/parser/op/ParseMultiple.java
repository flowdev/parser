package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseMultipleConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.ArrayList;


public class ParseMultiple<T> extends ParseWithSingleSubOp<T, ParseMultipleConfig> {
    public ParseMultiple(Params<T> params) {
        super(params);
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.tempStack.add(new ParserTempData(parserData.source.pos));
        parserData.subResults = new ArrayList<>(128);
        subOutPort.send(data);
    }

    @Override
    protected void handleSubOpData(T data) {
        final ParseMultipleConfig cfg = getVolatileConfig();
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.tempStack.get(parserData.tempStack.size() - 1);
        long count = tempData.subResults.size();
        if (parserData.result.matched) {
            tempData.subResults.add(parserData.result);
            count++;
            if (count >= cfg.max) {
                parserData.result = createMatchedResult(tempData.orgSrcPos,
                        parserData.source.content.substring(tempData.orgSrcPos, parserData.source.pos));
                parserData.subResults = tempData.subResults;
                semOutPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.result = null;
                subOutPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            if (count < cfg.min) {
                parserData.result = createUnmatchedResult(tempData.orgSrcPos);
                parserData.source.pos = tempData.orgSrcPos;
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.source.pos = parserData.result.pos;
                parserData.result = createMatchedResult(tempData.orgSrcPos,
                        parserData.source.content.substring(tempData.orgSrcPos, parserData.source.pos));
                parserData.subResults = tempData.subResults;
                semOutPort.send(params.setParserData.set(data, parserData));
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

    private static ParseResult createMatchedResult(int start, String text) {
        ParseResult result = new ParseResult();
        result.matched = true;
        result.pos = start;
        result.text = text;
        result.value = null;
        return result;
    }

    @Override
    public int parseSimple(String substring, ParseMultipleConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
