package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

import java.util.ArrayList;
import java.util.List;

import static org.flowdev.parser.util.ParserUtil.*;


public class ParseMultipleSync<T> extends ParseWithSingleSubOpSync<T, ParseMultipleSync.ParseMultipleSyncConfig> {
    public ParseMultipleSync(ParserParams<T> params) {
        super(params);
    }

    @Override
    public T parseAnySync(T data, ParseMultipleSyncConfig cfg) {
        ParserData parserData = params.getParserData.get(data);
        int orgSrcPos = parserData.getSource().pos();
        List<ParseResult> subResults = new ArrayList<>(1024);
        boolean matched = true;

        while (matched && subResults.size() < cfg.getMax()) {
            subOutPort.send(params.setParserData.set(data, parserData));
            data = dataFromSubOp;
            parserData = params.getParserData.get(data);
            matched = matched(parserData.getResult());
            if (matched) {
                subResults.add(parserData.getResult());
                parserData.setResult(null);
            }
        }


        if (subResults.size() >= cfg.getMin()) {
            createMatchedResult(parserData, subResults, orgSrcPos);
        } else {
            createUnmatchedResult(parserData, orgSrcPos);
        }

        return params.setParserData.set(data, parserData);
    }

    @Override
    protected void defaultSemantics(ParserData parserData) {
        if (getVolatileConfig().isUseTextSemantic()) {
            super.defaultSemantics(parserData);
        } else {
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
        }
    }

    private void createMatchedResult(ParserData parserData, List<ParseResult> subResults, int orgSrcPos) {
        int len = 0;
        if (!subResults.isEmpty()) {
            ParseResult lastSub = subResults.get(subResults.size() - 1);
            len = lastSub.getPos() + lastSub.getText().length() - orgSrcPos;
        }

        parserData.setResult(new ParseResult());
        parserData.getSource().setPos(orgSrcPos);
        parserData.setSubResults(subResults);
        fillResultMatched(parserData, len);
    }

    private void createUnmatchedResult(ParserData parserData, int orgSrcPos) {
        ParseResult result = parserData.getResult();

        parserData.setResult(new ParseResult());
        parserData.getSource().setPos(orgSrcPos);
        parserData.setSubResults(null);
        fillResultUnmatchedAbsolut(parserData, result.getErrPos(), result.getFeedback());
    }

    public static class ParseMultipleSyncConfig {
        private int min;
        private int max;
        private boolean useTextSemantic;

        public ParseMultipleSyncConfig(int min, int max, boolean useTextSemantic) {
            this.min = min;
            this.max = max;
            this.useTextSemantic = useTextSemantic;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public boolean isUseTextSemantic() {
            return useTextSemantic;
        }
    }
}
