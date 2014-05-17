package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseMultipleConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.ArrayList;
import java.util.List;

import static org.flowdev.parser.util.ParserUtil.*;


public class ParseMultiple<T> extends ParseWithSingleSubOp<T, ParseMultipleConfig> {
    public ParseMultiple(Params<T> params) {
        super(params);
        semInPort = data -> {
            ParserData parserData = params.getParserData.get(data);
            parserData.subResults = null;
            outPort.send(params.setParserData.set(data, parserData));
        };
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        if (parserData.tempStack == null) {
            parserData.tempStack = new ArrayList<>(128);
        }
        parserData.tempStack.add(new ParserTempData(parserData.source.pos));
        parserData.subResults = null;
        subOutPort.send(data);
    }

    @Override
    protected void handleSubOpData(T data) {
        final ParseMultipleConfig cfg = getVolatileConfig();
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.tempStack.get(parserData.tempStack.size() - 1);
        long count = tempData.subResults.size();

        if (matched(parserData.result)) {
            tempData.subResults.add(parserData.result);
            count++;
            if (count >= cfg.max) {
                createMatchedResult(parserData, tempData);
                handleSemantics(data, parserData);
            } else {
                parserData.result = null;
                subOutPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            if (count < cfg.min) {
                createUnmatchedResult(parserData, tempData);
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                createMatchedResult(parserData, tempData);
                handleSemantics(data, parserData);
            }
        }
    }

    private void handleSemantics(T data, ParserData parserData) {
        if (semOutPort == null) {
            List<Object> result = new ArrayList<>(parserData.subResults.size());
            int n = 0;
            for (ParseResult subResult : parserData.subResults) {
                result.add(subResult.value);
                if (subResult.value != null) {
                    n++;
                }
            }
            if (n == 0) {
                result = null;
            }
            parserData.result.value = result;
            outPort.send(params.setParserData.set(data, parserData));
        } else {
            semOutPort.send(params.setParserData.set(data, parserData));
        }
    }

    private void createMatchedResult(ParserData parserData, ParserTempData tempData) {
        ParseResult result = parserData.result;
        int textLen = result.text == null ? 0 : result.text.length();
        int len = result.pos + textLen - tempData.orgSrcPos;
        parserData.result = new ParseResult();
        parserData.source.pos = tempData.orgSrcPos;
        parserData.subResults = tempData.subResults;
        parserData.tempStack.remove(parserData.tempStack.size() - 1);
        fillResultMatched(parserData, len);
    }

    private void createUnmatchedResult(ParserData parserData, ParserTempData tempData) {
        ParseResult result = parserData.result;
        parserData.result = new ParseResult();
        parserData.source.pos = tempData.orgSrcPos;
        parserData.subResults = null;
        parserData.tempStack.remove(parserData.tempStack.size() - 1);
        fillResultUnmatched(parserData, 0, result.feedback.errors.get(0));
        parserData.result.errPos = result.errPos;
    }

    @Override
    public void parseSimple(String substring, ParseMultipleConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
