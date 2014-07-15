package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.List;

import static org.flowdev.parser.util.ParserUtil.matched;


public class ParseAlternatives<T> extends ParseWithMultipleSubOp<T, NoConfig> {
    public ParseAlternatives(Params<T> params) {
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
            parserData.tempStack.remove(parserData.tempStack.size() - 1);
            if (semOutPort != null) {
                semOutPort.send(params.setParserData.set(data, parserData));
            } else {
                outPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            count++;
            tempData.subResults.add(parserData.result);
            parserData.source.pos = tempData.orgSrcPos;
            if (count >= subOutPorts.size()) {
                parserData.result = mergeErrors(tempData.subResults);
                parserData.tempStack.remove(parserData.tempStack.size() - 1);
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.result = null;
                subOutPorts.get(count).send(params.setParserData.set(data, parserData));
            }
        }
    }

    private static ParseResult mergeErrors(List<ParseResult> errors) {
        ParseResult result = null;
        for (ParseResult err : errors) {
            if (result == null || result.errPos < err.errPos) {
                result = err;
            } else {
                result.feedback.getInfos().addAll(err.feedback.getInfos());
                result.feedback.getWarnings().addAll(err.feedback.getWarnings());
                result.feedback.getErrors().addAll(err.feedback.getErrors());
            }
        }
        return result;
    }

    @Override
    public void parseSimple(String substring, NoConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
