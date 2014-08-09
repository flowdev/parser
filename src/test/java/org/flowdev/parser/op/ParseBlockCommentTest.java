package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseBlockComment.ParseBlockCommentConfig;

@RunWith(Parameterized.class)
public class ParseBlockCommentTest extends ParseSimpleTest<ParseBlockCommentConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseBlockCommentConfig config = new ParseBlockCommentConfig().commentStart("{[").commentEnd("]}");
        return asList( //
                makeTestData("no match", 0, " 123 ", config, 0, 0, null, 0, 1), //
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 1), //
                makeTestData("simple", 0, "{[ 123 ]}", config, -1, 0, "{[ 123 ]}", 9, 0), //
                makeTestData("simple 2", 4, "abcd{[ 123 ]}", config, -1, 4, "{[ 123 ]}", 13, 0), //
                makeTestData("simple 3", 2, "ab{[ 123 ]}cdefg", config, -1, 2, "{[ 123 ]}", 11, 0), //
                makeTestData("nested", 2, "ab{[ 1 {[ 2 ]} 3 ]}cdefg", config, -1, 2, "{[ 1 {[ 2 ]} 3 ]}", 19, 0), //
                makeTestData("nested 2", 2, "ab{[1{[2{[3\r\n4]}5]}6]}cdefg", config, -1, 2, "{[1{[2{[3\r\n4]}5]}6]}", 22, 0), //
                makeTestData("double", 2, "ab{[{[]}{[]}]}cdefg", config, -1, 2, "{[{[]}{[]}]}", 14, 0), //
                makeTestData("error", 2, "ab{[ 1 {[ 2 ]}cdefg", config, 14, 2, null, 14, 1) //
        );
    }

    public ParseBlockCommentTest(ParserData parserData, ParseBlockCommentConfig config, ParseResult expectedResult,
                                 int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseBlockCommentConfig> makeParser(ParserParams<ParserData> params) {
        return new ParseBlockComment<>(params);
    }
}
