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
        parserData.getTempStack().add(new ParserTempData(parserData.getSource().getPos()));
        subOutPorts.get(0).send(params.setParserData.set(data, parserData));
    }

    @Override
    protected void handleSubOpData(T data) {
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.getTempStack().get(parserData.getTempStack().size() - 1);
        int count = tempData.getSubResults().size();
        if (matched(parserData.getResult())) {
            parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
            if (semOutPort != null) {
                semOutPort.send(params.setParserData.set(data, parserData));
            } else {
                outPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            count++;
            tempData.getSubResults().add(parserData.getResult());
            parserData.getSource().setPos(tempData.getOrgSrcPos());
            if (count >= subOutPorts.size()) {
                parserData.setResult(mergeErrors(tempData.getSubResults()));
                parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
                outPort.send(params.setParserData.set(data, parserData));
            } else {
                parserData.setResult(null);
                subOutPorts.get(count).send(params.setParserData.set(data, parserData));
            }
        }
    }

    private static ParseResult mergeErrors(List<ParseResult> errors) {
        ParseResult result = null;
        for (ParseResult err : errors) {
            if (result == null || result.getErrPos() < err.getErrPos()) {
                result = err;
            } else {
                result.getFeedback().getInfos().addAll(err.getFeedback().getInfos());
                result.getFeedback().getWarnings().addAll(err.getFeedback().getWarnings());
                result.getFeedback().getErrors().addAll(err.getFeedback().getErrors());
            }
        }
        return result;
    }

    @Override
    public void parseSimple(String substring, NoConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
