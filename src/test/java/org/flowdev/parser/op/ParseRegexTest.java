package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class ParseRegexTest extends ParseSimpleTest<ParseRegex.ParseRegexConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseRegex.ParseRegexConfig config = new ParseRegex.ParseRegexConfig("[a]+");
        return asList( //
                makeTestData("no match", 0, "baaa", config, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 1), //
                makeTestData("simple", 0, "a", config, -1, 0, "a", 1, 0), //
                makeTestData("simple 2", 0, "aaa 123", config, -1, 0, "aaa", 3, 0), //
                makeTestData("simple 3", 2, "12aaa3456", config, -1, 2, "aaa", 5, 0)  //
        );
    }

    public ParseRegexTest(ParserData parserData, ParseRegex.ParseRegexConfig config, ParseResult expectedResult,
                          int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseRegex.ParseRegexConfig> makeParser(ParserParams<ParserData> params) {
        return new ParseRegex<>(params);
    }
}
