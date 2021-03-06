package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
import static org.flowdev.parser.util.ParserUtil.*;


public class ParseMultiple<T> extends ParseWithSingleSubOp<T, ParseMultiple.ParseMultipleConfig> {
    public ParseMultiple(ParserParams<T> params) {
        super(params);
    }

    @Override
    public T parseAny(T data, ParseMultipleConfig cfg) {
        ParserData parserData = params.getParserData.get(data);
        int orgSrcPos = parserData.source().pos();
        List<ParseResult> subResults = new ArrayList<>(min(cfg.max(), 128));
        boolean matched = true;

        while (matched && subResults.size() < cfg.max()) {
            subOutPort.send(params.setParserData.set(data, parserData));
            data = dataFromSubOp;
            parserData = params.getParserData.get(data);
            ParseResult result = parserData.result();
            matched = matched(result);
            if (matched) {
                subResults.add(result);
                parserData.result(null);
            }
        }

        if (subResults.size() >= cfg.min()) {
            createMatchedResult(parserData, subResults, orgSrcPos);
        } else {
            createUnmatchedResult(parserData, orgSrcPos);
        }

        return params.setParserData.set(data, parserData);
    }

    @Override
    protected void defaultSemantics(ParserData parserData) {
        if (getVolatileConfig().useTextSemantic()) {
            super.defaultSemantics(parserData);
        } else {
            List<Object> result = new ArrayList<>(parserData.subResults().size());
            int n = 0;
            for (ParseResult subResult : parserData.subResults()) {
                result.add(subResult.value());
                if (subResult.value() != null) {
                    n++;
                }
            }
            if (n == 0) {
                result = null;
            }
            parserData.result().value(result);
        }
    }

    private void createMatchedResult(ParserData parserData, List<ParseResult> subResults, int orgSrcPos) {
        int len = 0;
        if (!subResults.isEmpty()) {
            ParseResult lastSub = subResults.get(subResults.size() - 1);
            len = lastSub.pos() + lastSub.text().length() - orgSrcPos;
        }

        parserData.result(new ParseResult()).subResults(subResults).source().pos(orgSrcPos);
        fillResultMatched(parserData, len);
    }

    private void createUnmatchedResult(ParserData parserData, int orgSrcPos) {
        ParseResult result = parserData.result();

        parserData.result(new ParseResult()).subResults(null).source().pos(orgSrcPos);
        fillResultUnmatchedAbsolut(parserData, result.errPos(), result.feedback());
    }

    public static class ParseMultipleConfig {
        private int min;
        private int max;
        private boolean useTextSemantic;

        public ParseMultipleConfig min(final int min) {
            this.min = min;
            return this;
        }

        public ParseMultipleConfig max(final int max) {
            this.max = max;
            return this;
        }

        public ParseMultipleConfig useTextSemantic(final boolean useTextSemantic) {
            this.useTextSemantic = useTextSemantic;
            return this;
        }

        public int min() {
            return min;
        }

        public int max() {
            return max;
        }

        public boolean useTextSemantic() {
            return useTextSemantic;
        }
    }
}
