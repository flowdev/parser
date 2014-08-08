package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.flowdev.parser.util.ParserUtil.*;


public class ParseAllSync<T> extends ParseWithMultipleSubOpSync<T, UseTextSemanticConfig> {
    public ParseAllSync(ParserParams<T> params) {
        super(params);
    }

    @Override
    public T parseAnySync(T data, UseTextSemanticConfig cfg) {
        ParserData parserData = params.getParserData.get(data);
        int orgSrcPos = parserData.getSource().getPos();
        List<ParseResult> subResults = new ArrayList<>(128);
        boolean matched = true;

        while (matched && subResults.size() < subOutPorts.size()) {
            subOutPorts.get(subResults.size()).send(params.setParserData.set(data, parserData));
            data = dataFromSubOp;
            parserData = params.getParserData.get(data);
            matched = matched(parserData.getResult());
            if (matched) {
                subResults.add(parserData.getResult());
                parserData.setResult(null);
            }
        }


        if (matched) {
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
            List<Object> result =
                    parserData.getSubResults().stream().map(ParseResult::getValue).collect(Collectors.toList());
            parserData.getResult().setValue(result);
        }
    }

    private void createMatchedResult(ParserData parserData, List<ParseResult> subResults, int orgSrcPos) {
        ParseResult lastSub = subResults.get(subResults.size() - 1);
        int len = lastSub.getPos() + lastSub.getText().length() - orgSrcPos;

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
}
