package org.flowdev.parser.op;

import org.flowdev.base.data.Feedback;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.SourceData;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.flowdev.parser.op.ParseSimple.Params;
import static org.flowdev.parser.util.ParserUtil.matched;
import static org.junit.Assert.assertEquals;

public abstract class ParseSimpleTest<C> {
    ParserData parserData;
    private ParseResult expectedResult;
    private int expectedSrcPos;
    private int expectedErrorCount;
    private ParseSimple<ParserData, C> parser;
    protected boolean dontRunTests = false;

    public ParseSimpleTest(ParserData parserData, C config, ParseResult expectedResult, int expectedSrcPos, int expectedErrorCount) {
        this.parserData = parserData;
        this.expectedResult = expectedResult;
        this.expectedSrcPos = expectedSrcPos;
        this.expectedErrorCount = expectedErrorCount;

        Params<ParserData> params = new Params<>();
        params.getParserData = data -> data;
        params.setParserData = (data, subdata) -> {
            data = subdata;
            return subdata;
        };
        this.parser = makeParser(params);
        this.parser.setErrorPort(e -> {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        });
        this.parser.setOutPort(data -> { /* nothing to do */ });
        this.parser.getConfigPort().send(config);
    }

    protected abstract ParseSimple<ParserData, C> makeParser(Params<ParserData> params);

    @Test
    public void testParser() {
        if (dontRunTests) {
            return;
        }
        parser.getInPort().send(parserData);
        assertEquals("unexpected error position:", expectedResult.errPos, parserData.result.errPos);
        assertEquals("unexpected source position:", expectedSrcPos, parserData.source.pos);
        if (matched(expectedResult)) {
            assertEquals("unexpected result position:", expectedResult.pos, parserData.result.pos);
            assertEquals("unexpected result text:", expectedResult.text, parserData.result.text);
        }
        assertEquals("unexpected error count:", expectedErrorCount, getActualErrorCount(parserData));
    }

    private static int getActualErrorCount(ParserData parserData) {
        if (parserData.result.feedback == null) {
            return 0;
        }
        if (parserData.result.feedback.errors == null) {
            return 0;
        }
        return parserData.result.feedback.errors.size();
    }

    public static Object[] makeTestData(String srcName, int srcPos, String srcContent, Object config, int errPos,
                                        int resultPos, String resultText, int newSrcPos, int errorCount) {
        ParserData parserData = new ParserData();
        parserData.source = new SourceData();
        parserData.source.name = srcName;
        parserData.source.pos = srcPos;
        parserData.source.content = srcContent;
        parserData.feedback = new Feedback();
        parserData.feedback.errors = new ArrayList<>();
        parserData.feedback.warnings = new ArrayList<>();
        parserData.feedback.infos = new ArrayList<>();

        ParseResult parseResult = new ParseResult();
        parseResult.errPos = errPos;
        parseResult.pos = resultPos;
        parseResult.text = resultText;

        return new Object[]{parserData, config, parseResult, newSrcPos, errorCount};
    }
}
