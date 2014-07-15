package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseEofTest extends ParseSimpleTest<NoConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        return asList( //
                makeTestData("no match", 2, "12flow345", null, 2, 0, null, 2, 1), //
                makeTestData("empty", 0, "", null, -1, 0, "", 0, 0), //
                makeTestData("simple", 4, "flow", null, -1, 4, "", 4, 0), //
                makeTestData("simple 2", 8, "flow 123", null, -1, 8, "", 8, 0) //
        );
    }

    public ParseEofTest(ParserData parserData, NoConfig config, ParseResult expectedResult,
                        int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, NoConfig> makeParser(Params<ParserData> params) {
        return new ParseEof<>(params);
    }
}
