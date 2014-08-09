package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSpace.ParseSpaceConfig;

@RunWith(Parameterized.class)
public class ParseSpaceTest extends ParseSimpleTest<ParseSpaceConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseSpaceConfig incNlCfg = new ParseSpaceConfig().acceptNewline(true);
        ParseSpaceConfig excNlCfg = new ParseSpaceConfig().acceptNewline(false);
        return asList( //
                makeTestData("no match", 0, "ba", incNlCfg, 0, 0, null, 0, 1), //
                makeTestData("no match", 0, "ba", excNlCfg, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", incNlCfg, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", excNlCfg, 0, 0, null, 0, 1), //
                makeTestData("simple", 0, " ", incNlCfg, -1, 0, " ", 1, 0), //
                makeTestData("simple", 0, " ", excNlCfg, -1, 0, " ", 1, 0), //
                makeTestData("simple 2", 0, " \t\r\n 123", incNlCfg, -1, 0, " \t\r\n ", 5, 0), //
                makeTestData("simple 2", 0, " \t\r\n 123", excNlCfg, -1, 0, " \t\r", 3, 0), //
                makeTestData("simple 3", 2, "12 \t\r\n 3456", incNlCfg, -1, 2, " \t\r\n ", 7, 0), //
                makeTestData("simple 3", 2, "12 \t\r\n 3456", excNlCfg, -1, 2, " \t\r", 5, 0) //
        );
    }

    public ParseSpaceTest(ParserData parserData, ParseSpaceConfig config, ParseResult expectedResult,
                          int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseSpaceConfig> makeParser(ParserParams<ParserData> params) {
        return new ParseSpace<>(params);
    }
}
