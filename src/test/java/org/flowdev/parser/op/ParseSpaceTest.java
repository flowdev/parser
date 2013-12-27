package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParseSpaceConfig;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseSpaceTest extends ParseSimpleTest<ParseSpaceConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseSpaceConfig incNlCfg = new ParseSpaceConfig(true);
        ParseSpaceConfig excNlCfg = new ParseSpaceConfig(false);
        return asList( //
                makeTestData("no match", 0, "ba", incNlCfg, false, 0, null, 0, 0), //
                makeTestData("no match", 0, "ba", excNlCfg, false, 0, null, 0, 0), //
                makeTestData("empty", 0, "", incNlCfg, false, 0, null, 0, 0), //
                makeTestData("empty", 0, "", excNlCfg, false, 0, null, 0, 0), //
                makeTestData("simple", 0, " ", incNlCfg, true, 0, " ", 1, 0), //
                makeTestData("simple", 0, " ", excNlCfg, true, 0, " ", 1, 0), //
                makeTestData("simple 2", 0, " \t\r\n 123", incNlCfg, true, 0, " \t\r\n ", 5, 0), //
                makeTestData("simple 2", 0, " \t\r\n 123", excNlCfg, true, 0, " \t\r", 3, 0), //
                makeTestData("simple 3", 2, "12 \t\r\n 3456", incNlCfg, true, 2, " \t\r\n ", 7, 0), //
                makeTestData("simple 3", 2, "12 \t\r\n 3456", excNlCfg, true, 2, " \t\r", 5, 0) //
        );
    }

    public ParseSpaceTest(ParserData parserData, ParseSpaceConfig config, ParseResult expectedResult,
                          int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseSpaceConfig> makeParser(Params<ParserData> params) {
        return new ParseSpace<>(params);
    }
}
