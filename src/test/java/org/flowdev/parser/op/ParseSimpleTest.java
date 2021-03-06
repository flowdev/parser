package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.SourceData;
import org.junit.Assert;
import org.junit.Test;

import static org.flowdev.parser.util.ParserUtil.matched;
import static org.junit.Assert.assertEquals;

public abstract class ParseSimpleTest<C> {
    final ParserData parserData;
    private final ParseResult expectedResult;
    private final int expectedSrcPos;
    private final int expectedErrorCount;
    private final BaseParser<ParserData, C> parser;
    protected boolean dontRunTests = false;

    public ParseSimpleTest(ParserData parserData, C config, ParseResult expectedResult, int expectedSrcPos, int expectedErrorCount) {
        this.parserData = parserData;
        this.expectedResult = expectedResult;
        this.expectedSrcPos = expectedSrcPos;
        this.expectedErrorCount = expectedErrorCount;

        ParserParams<ParserData> params = new ParserParams<>();
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

    protected abstract BaseParser<ParserData, C> makeParser(ParserParams<ParserData> params);

    @Test
    public void testParser() {
        if (dontRunTests) {
            return;
        }
        parser.getInPort().send(parserData);
        assertEquals("unexpected error position:", expectedResult.errPos(), parserData.result().errPos());
        assertEquals("unexpected source position:", expectedSrcPos, parserData.source().pos());
        if (matched(expectedResult)) {
            assertEquals("unexpected result position:", expectedResult.pos(), parserData.result().pos());
            assertEquals("unexpected result text:", expectedResult.text(), parserData.result().text());
        }
        assertEquals("unexpected error count:", expectedErrorCount, getActualErrorCount(parserData));
    }

    private static int getActualErrorCount(ParserData parserData) {
        if (parserData.result().feedback() == null) {
            return 0;
        }
        if (parserData.result().feedback().getErrors() == null) {
            return 0;
        }
        return parserData.result().feedback().getErrors().size();
    }

    public static Object[] makeTestData(String srcName, int srcPos, String srcContent, Object config, int errPos,
                                        int resultPos, String resultText, int newSrcPos, int errorCount) {
        ParserData parserData = new ParserData().source(new SourceData().name(srcName).pos(srcPos).content(srcContent));

        ParseResult parseResult = new ParseResult().errPos(errPos).pos(resultPos).text(resultText);

        return new Object[]{parserData, config, parseResult, newSrcPos, errorCount};
    }
}
