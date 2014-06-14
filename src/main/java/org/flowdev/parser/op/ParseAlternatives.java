package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.List;

import static org.flowdev.parser.util.ParserUtil.matched;


public class ParseAlternatives<T> extends ParseWithMultipleSubOp<T, EmptyConfig> {
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
        if (matched(parserData.result)) {
            parserData.tempStack.remove(parserData.tempStack.size() - 1);
            if (semOutPort != null) {
                semOutPort.send(params.setParserData.set(data, parserData));
            } else {
                outPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            tempData.index++;
            if (tempData.index >= subOutPorts.size()) {
                tempData.subResults.add(parserData.result);
                parserData.result = mergeErrors(tempData.subResults);
                parserData.source.pos = tempData.orgSrcPos;
                parserData.tempStack.remove(parserData.tempStack.size() - 1);
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                tempData.subResults.add(parserData.result);
                parserData.result = null;
                parserData.source.pos = tempData.orgSrcPos;
                subOutPorts.get(tempData.index).send(params.setParserData.set(data, parserData));
            }
        }
    }

    private static ParseResult mergeErrors(List<ParseResult> errors) {
        ParseResult result = null;
        for (ParseResult err : errors) {
            if (result == null || result.errPos < err.errPos) {
                result = err;
            } else {
                result.feedback.infos.addAll(err.feedback.infos);
                result.feedback.warnings.addAll(err.feedback.warnings);
                result.feedback.errors.addAll(err.feedback.errors);
            }
        }
        return result;
    }

    @Override
    public void parseSimple(String substring, EmptyConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
