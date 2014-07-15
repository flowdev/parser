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
        parserData.getTempStack().add(new ParserTempData(parserData.getSource().getPos()));
        subOutPorts.get(0).send(params.setParserData.set(data, parserData));
    }

    @Override
    protected void handleSubOpData(T data) {
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.getTempStack().get(parserData.getTempStack().size() - 1);
        int count = tempData.getSubResults().size();
        if (matched(parserData.getResult())) {
            tempData.getSubResults().add(parserData.getResult());
            count++;
            if (count >= subOutPorts.size()) {
                parserData.setResult(mergeResults(tempData.getSubResults(), parserData.getSource()));
                parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
                if (semOutPort != null) {
                    parserData.setSubResults(tempData.getSubResults());
                    semOutPort.send(params.setParserData.set(data, parserData));
                } else {
                    parserData.getResult().setValue(mergeValues(tempData.getSubResults()));
                    outPort.send(params.setParserData.set(data, parserData));
                }
            } else {
                parserData.setResult(null);
                subOutPorts.get(count).send(params.setParserData.set(data, parserData));
            }
        } else {
            parserData.getSource().setPos(tempData.getOrgSrcPos());
            parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
            outPort.send(params.setParserData.set(data, parserData));
        }
    }

    private Object mergeValues(List<ParseResult> subResults) {
        List<Object> allValue = new ArrayList<>(subResults.size());
        for (ParseResult subResult : subResults) {
            allValue.add(subResult.getValue());
        }
        return allValue;
    }

    private ParseResult mergeResults(List<ParseResult> subResults, SourceData source) {
        ParseResult result = subResults.get(0);
        result.setText(source.getContent().substring(result.getPos(), source.getPos()));
        return result;
    }

    @Override
    public void parseSimple(String substring, NoConfig cfg, ParserData parserData) {
        throw new UnsupportedOperationException("The filter method should handle everything itself!");
    }

}
