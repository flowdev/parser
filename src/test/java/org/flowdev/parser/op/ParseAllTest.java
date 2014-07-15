package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.BaseParser.Params;

@RunWith(Parameterized.class)
public class ParseAllTest extends ParseSimpleTest<NoConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        return asList( //
                // name, srcPos, content, config, errPos, resultPos, resultText, newSrcPos, errCount
                makeTestData("empty", 0, "", null, 0, 0, null, 0, 1), //
                makeTestData("no match", 0, " flow no", null, 0, 0, null, 0, 1), //
                makeTestData("match flow", 0, "flowabc", null, 4, 0, null, 0, 1), //
                makeTestData("match no", 3, "123noabc", null, 3, 0, null, 3, 1), //
                makeTestData("match all", 3, "123flownoabc", null, -1, 3, "flowno", 9, 0) //
        );
    }

    public ParseAllTest(ParserData parserData, NoConfig config, ParseResult expectedResult,
                        int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, NoConfig> makeParser(Params<ParserData> params) {
        ParseLiteral<ParserData> parseLiteralFlow = new ParseLiteral<>(params);
        parseLiteralFlow.getConfigPort().send(new ParseLiteral.ParseLiteralConfig("flow"));

        ParseLiteral<ParserData> parseLiteralNo = new ParseLiteral<>(params);
        parseLiteralNo.getConfigPort().send(new ParseLiteral.ParseLiteralConfig("no"));

        ParseAll<ParserData> parseAll = new ParseAll<>(params);
        parseAll.setSubOutPort(0, parseLiteralFlow.getInPort());
        parseAll.setSubOutPort(1, parseLiteralNo.getInPort());
        parseLiteralFlow.setOutPort(parseAll.getSubInPort());
        parseLiteralNo.setOutPort(parseAll.getSubInPort());

        return parseAll;
    }
}
