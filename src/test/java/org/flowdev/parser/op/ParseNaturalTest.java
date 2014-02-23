package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseNaturalConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseNaturalTest extends ParseSimpleTest<ParseNaturalConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseNaturalConfig config = new ParseNaturalConfig(10);
        return asList( //
                makeTestData("no match", 0, "baaa", config, 0, 0, null, 0, 0), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 0), //
                makeTestData("simple", 0, "3", config, -1, 0, "3", 1, 0), //
                makeTestData("simple 2", 0, "123", config, -1, 0, "123", 3, 0), //
                makeTestData("simple 3", 2, "ab123c456", config, -1, 2, "123", 5, 0), //
                makeTestData("error", 0, "1234567890123456789012345678901234567890", config, 0, 0, null, 0, 1)  //
        );
    }

    public ParseNaturalTest(ParserData parserData, ParseNaturalConfig config, ParseResult expectedResult,
                            int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseNaturalConfig> makeParser(Params<ParserData> params) {
        return new ParseNatural<>(params);
    }
}
