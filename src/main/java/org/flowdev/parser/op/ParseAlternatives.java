package org.flowdev.parser.op;

import org.flowdev.base.data.Feedback;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;

import static org.flowdev.parser.util.ParserUtil.matched;


public class ParseAlternatives<T> extends ParseWithMultipleSubOp<T, UseTextSemanticConfig> {
    public ParseAlternatives(ParserParams<T> params) {
        super(params);
    }

    @Override
    public T parseAnySync(T data, UseTextSemanticConfig cfg) {
        ParserData parserData = params.getParserData.get(data);
        Feedback allFeedback = new Feedback();
        int minErrPos = Integer.MAX_VALUE;
        boolean matched = false;

        for (int i = 0; !matched && i < subOutPorts.size(); i++) {
            parserData.result(null);
            subOutPorts.get(i).send(params.setParserData.set(data, parserData));
            data = dataFromSubOp;
            parserData = params.getParserData.get(data);
            ParseResult result = parserData.result();
            matched = matched(result);

            if (!matched) {
                minErrPos = addFeedback(result, allFeedback, minErrPos);
            }
        }

        if (!matched) {
            parserData.result().errPos(minErrPos).feedback(allFeedback);
        }

        return params.setParserData.set(data, parserData);
    }

    private int addFeedback(ParseResult result, Feedback allFeedback, int minErrPos) {
        allFeedback.errors().addAll(result.feedback().errors());
        allFeedback.warnings().addAll(result.feedback().warnings());
        allFeedback.infos().addAll(result.feedback().infos());

        return (result.errPos() < minErrPos) ? result.errPos() : minErrPos;
    }

    @Override
    protected void defaultSemantics(ParserData parserData) {
        if (getVolatileConfig().useTextSemantic()) {
            super.defaultSemantics(parserData);
        }
        // else: the result has been set already by the matching sub operation
    }
}
