package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class ParseLiteralTest extends ParseSimpleTest<ParseLiteral.ParseLiteralConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseLiteral.ParseLiteralConfig config = new ParseLiteral.ParseLiteralConfig("flow");
        return asList( //
                makeTestData("no match", 0, " flow", config, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 1), //
                makeTestData("simple", 0, "flow", config, -1, 0, "flow", 4, 0), //
                makeTestData("simple 2", 0, "flow 123", config, -1, 0, "flow", 4, 0), //
                makeTestData("simple 3", 2, "12flow345", config, -1, 2, "flow", 6, 0)  //
        );
    }

    public ParseLiteralTest(ParserData parserData, ParseLiteral.ParseLiteralConfig config, ParseResult expectedResult,
                            int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseLiteral.ParseLiteralConfig> makeParser(ParserParams<ParserData> params) {
        return new ParseLiteral<>(params);
    }
}
