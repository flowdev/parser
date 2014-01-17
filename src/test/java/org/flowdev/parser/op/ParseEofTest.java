package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseEofTest extends ParseSimpleTest<EmptyConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        return asList( //
                makeTestData("no match", 2, "12flow345", null, false, 0, null, 2, 0), //
                makeTestData("empty", 0, "", null, true, 0, null, 0, 0), //
                makeTestData("simple", 4, "flow", null, true, 4, null, 4, 0), //
                makeTestData("simple 2", 8, "flow 123", null, true, 8, null, 8, 0) //
        );
    }

    public ParseEofTest(ParserData parserData, EmptyConfig config, ParseResult expectedResult,
                        int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, EmptyConfig> makeParser(Params<ParserData> params) {
        return new ParseEof<>(params);
    }
}
