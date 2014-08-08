package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class ParseLineCommentTest extends ParseSimpleTest<ParseLineComment.ParseLineCommentConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseLineComment.ParseLineCommentConfig config = new ParseLineComment.ParseLineCommentConfig("//");
        return asList( //
                makeTestData("no match", 0, " // ", config, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 1), //
                makeTestData("simple", 0, "// 1\n", config, -1, 0, "// 1", 4, 0), //
                makeTestData("simple 2", 0, "// 1\n 23", config, -1, 0, "// 1", 4, 0), //
                makeTestData("simple 3", 2, "12// 1\n345", config, -1, 2, "// 1", 6, 0), //
                makeTestData("simple 4", 2, "12// 1\r\n345", config, -1, 2, "// 1\r", 7, 0) //
        );
    }

    public ParseLineCommentTest(ParserData parserData, ParseLineComment.ParseLineCommentConfig config, ParseResult expectedResult,
                                int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseLineComment.ParseLineCommentConfig> makeParser(ParserParams<ParserData> params) {
        return new ParseLineComment<>(params);
    }
}
