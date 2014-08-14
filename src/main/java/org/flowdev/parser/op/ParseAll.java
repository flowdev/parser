package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.flowdev.parser.util.ParserUtil.*;


public class ParseAll<T> extends ParseWithMultipleSubOp<T, UseTextSemanticConfig> {
    public ParseAll(ParserParams<T> params) {
        super(params);
        getConfigPort().send(new UseTextSemanticConfig().useTextSemantic(false));
    }

    @Override
    public T parseAny(T data, UseTextSemanticConfig cfg) {
        ParserData parserData = params.getParserData.get(data);
        int orgSrcPos = parserData.source().pos();
        List<ParseResult> subResults = new ArrayList<>(subOutPorts.size());
        boolean matched = true;

        while (matched && subResults.size() < subOutPorts.size()) {
            subOutPorts.get(subResults.size()).send(params.setParserData.set(data, parserData));
            data = dataFromSubOp;
            parserData = params.getParserData.get(data);
            matched = matched(parserData.result());
            if (matched) {
                subResults.add(parserData.result());
                parserData.result(null);
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
        if (getVolatileConfig().useTextSemantic()) {
            super.defaultSemantics(parserData);
        } else {
            List<Object> result =
                    parserData.subResults().stream().map(ParseResult::value).collect(Collectors.toList());
            parserData.result().value(result);
        }
    }

    private void createMatchedResult(ParserData parserData, List<ParseResult> subResults, int orgSrcPos) {
        ParseResult lastSub = subResults.get(subResults.size() - 1);
        int len = lastSub.pos() + lastSub.text().length() - orgSrcPos;

        parserData.result(new ParseResult()).subResults(subResults).source().pos(orgSrcPos);
        fillResultMatched(parserData, len);
    }

    private void createUnmatchedResult(ParserData parserData, int orgSrcPos) {
        ParseResult errResult = parserData.result();

        parserData.result(new ParseResult()).subResults(null).source().pos(orgSrcPos);
        fillResultUnmatchedAbsolut(parserData, errResult.errPos(), errResult.feedback());
    }
}
