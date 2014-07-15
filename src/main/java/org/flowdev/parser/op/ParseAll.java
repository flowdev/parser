package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;
import org.flowdev.parser.data.SourceData;

import java.util.ArrayList;
import java.util.List;

import static org.flowdev.parser.util.ParserUtil.matched;


public class ParseAll<T> extends ParseWithMultipleSubOp<T, NoConfig> {
    public ParseAll(Params<T> params) {
        super(params);
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        parserData.tempStack.add(new ParserTempData(parserData.source.pos));
        subOutPorts.get(0).send(params.setParserData.set(data, parserData));
    }

    @Override
    protected void handleSubOpData(T data) {
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.tempStack.get(parserData.tempStack.size() - 1);
        int count = tempData.subResults.size();
        if (matched(parserData.result)) {
            tempData.subResults.add(parserData.result);
            count++;
            if (count >= subOutPorts.size()) {
                parserData.result = mergeResults(tempData.subResults, parserData.source);
                parserData.tempStack.remove(parserData.tempStack.size() - 1);
                if (semOutPort != null) {
                    parserData.subResults = tempData.subResults;
                    semOutPort.send(params.setParserData.set(data, parserData));
                } else {
                    parserData.result.value = mergeValues(tempData.subResults);
                    outPort.send(params.setParserData.set(data, parserData));
                }
            } else {
                parserData.result = null;
                subOutPorts.get(count).send(params.setParserData.set(data, parserData));
            }
        } else {
            parserData.source.pos = tempData.orgSrcPos;
            parserData.tempStack.remove(parserData.tempStack.size() - 1);
            outPort.send(params.setParserData.set(data, parserData));
        }
    }

    private Object mergeValues(List<ParseResult> subResults) {
        List<Object> allValue = new ArrayList<>(subResults.size());
        for (ParseResult subResult : subResults) {
            allValue.add(subResult.value);
        }
        return allValue;
    }

    private ParseResult mergeResults(List<ParseResult> subResults, SourceData source) {
        ParseResult result = subResults.get(0);
        result.text = source.content.substring(result.pos, source.pos);
        return result;
    }

    @Override
    public void parseSimple(String substring, NoConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
