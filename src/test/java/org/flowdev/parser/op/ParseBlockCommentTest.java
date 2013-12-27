package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseBlockCommentConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseSimple.Params;

@RunWith(Parameterized.class)
public class ParseBlockCommentTest extends ParseSimpleTest<ParseBlockCommentConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseBlockCommentConfig config = new ParseBlockCommentConfig("{[", "]}");
        return asList( //
                makeTestData("no match", 0, " 123 ", config, false, 0, null, 0, 0), //
                makeTestData("empty", 0, "", config, false, 0, null, 0, 0), //
                makeTestData("simple", 0, "{[ 123 ]}", config, true, 0, "{[ 123 ]}", 9, 0), //
                makeTestData("simple 2", 4, "abcd{[ 123 ]}", config, true, 4, "{[ 123 ]}", 13, 0), //
                makeTestData("simple 3", 2, "ab{[ 123 ]}cdefg", config, true, 2, "{[ 123 ]}", 11, 0), //
                makeTestData("nested", 2, "ab{[ 1 {[ 2 ]} 3 ]}cdefg", config, true, 2, "{[ 1 {[ 2 ]} 3 ]}", 19, 0), //
                makeTestData("nested 2", 2, "ab{[1{[2{[3\r\n4]}5]}6]}cdefg", config, true, 2, "{[1{[2{[3\r\n4]}5]}6]}", 22, 0), //
                makeTestData("double", 2, "ab{[{[]}{[]}]}cdefg", config, true, 2, "{[{[]}{[]}]}", 14, 0) //
        );
    }

    public ParseBlockCommentTest(ParserData parserData, ParseBlockCommentConfig config, ParseResult expectedResult,
                                 int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseBlockCommentConfig> makeParser(Params<ParserData> params) {
        return new ParseBlockComment<>(params);
    }
}
