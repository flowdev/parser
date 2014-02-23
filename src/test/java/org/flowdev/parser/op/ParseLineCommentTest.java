package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseLineCommentConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseLineCommentTest extends ParseSimpleTest<ParseLineCommentConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseLineCommentConfig config = new ParseLineCommentConfig("//");
        return asList( //
                makeTestData("no match", 0, " // ", config, 0, 0, null, 0, 0), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 0), //
                makeTestData("simple", 0, "// 1\n", config, -1, 0, "// 1", 4, 0), //
                makeTestData("simple 2", 0, "// 1\n 23", config, -1, 0, "// 1", 4, 0), //
                makeTestData("simple 3", 2, "12// 1\n345", config, -1, 2, "// 1", 6, 0), //
                makeTestData("simple 4", 2, "12// 1\r\n345", config, -1, 2, "// 1\r", 7, 0) //
        );
    }

    public ParseLineCommentTest(ParserData parserData, ParseLineCommentConfig config, ParseResult expectedResult,
                                int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseLineCommentConfig> makeParser(Params<ParserData> params) {
        ParseLineComment<ParserData> parser = new ParseLineComment<>(params);
        return parser;
    }
}
