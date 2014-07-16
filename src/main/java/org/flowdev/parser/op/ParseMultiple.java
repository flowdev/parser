package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.ParserTempData;

import java.util.ArrayList;
import java.util.List;

import static org.flowdev.parser.util.ParserUtil.*;


public class ParseMultiple<T> extends ParseWithSingleSubOp<T, ParseMultiple.ParseMultipleConfig> {
    public ParseMultiple(Params<T> params) {
        super(params);
        semInPort = data -> {
            ParserData parserData = params.getParserData.get(data);
            parserData.setSubResults(null);
            outPort.send(params.setParserData.set(data, parserData));
        };
    }

    @Override
    public void filter(T data) {
        ParserData parserData = params.getParserData.get(data);
        if (parserData.getTempStack() == null) {
            parserData.setTempStack(new ArrayList<>(128));
        }
        parserData.getTempStack().add(new ParserTempData(parserData.getSource().getPos()));
        parserData.setSubResults(null);
        subOutPort.send(data);
    }

    @Override
    protected void handleSubOpData(T data) {
        final ParseMultipleConfig cfg = getVolatileConfig();
        ParserData parserData = params.getParserData.get(data);
        ParserTempData tempData = parserData.getTempStack().get(parserData.getTempStack().size() - 1);
        long count = tempData.getSubResults().size();

        if (matched(parserData.getResult())) {
            tempData.getSubResults().add(parserData.getResult());
            count++;
            if (count >= cfg.getMax()) {
                createMatchedResult(parserData, tempData);
                handleSemantics(data, parserData);
            } else {
                parserData.setResult(null);
                subOutPort.send(params.setParserData.set(data, parserData));
            }
        } else {
            if (count < cfg.getMin()) {
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
            List<Object> result = new ArrayList<>(parserData.getSubResults().size());
            int n = 0;
            for (ParseResult subResult : parserData.getSubResults()) {
                result.add(subResult.getValue());
                if (subResult.getValue() != null) {
                    n++;
                }
            }
            if (n == 0) {
                result = null;
            }
            parserData.getResult().setValue(result);
            outPort.send(params.setParserData.set(data, parserData));
        } else {
            semOutPort.send(params.setParserData.set(data, parserData));
        }
    }

    private void createMatchedResult(ParserData parserData, ParserTempData tempData) {
        ParseResult result = parserData.getResult();
        int textLen = result.getText() == null ? 0 : result.getText().length();
        int len = result.getPos() + textLen - tempData.getOrgSrcPos();

        parserData.setResult(new ParseResult());
        parserData.getSource().setPos(tempData.getOrgSrcPos());
        parserData.setSubResults(tempData.getSubResults());
        parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
        fillResultMatched(parserData, len);
    }

    private void createUnmatchedResult(ParserData parserData, ParserTempData tempData) {
        ParseResult result = parserData.getResult();
        parserData.setResult(new ParseResult());
        parserData.getSource().setPos(tempData.getOrgSrcPos());
        parserData.setSubResults(null);
        parserData.getTempStack().remove(parserData.getTempStack().size() - 1);
        fillResultUnmatched(parserData, 0, result.getFeedback().getErrors().get(0));
        parserData.getResult().setErrPos(result.getErrPos());
    }

    public static class ParseMultipleConfig {
        private int min;
        private int max;

        public ParseMultipleConfig(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }
}
