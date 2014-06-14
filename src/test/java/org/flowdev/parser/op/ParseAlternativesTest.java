package org.flowdev.parser.op;

import org.flowdev.base.data.EmptyConfig;
import org.flowdev.parser.data.ParseLiteralConfig;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.BaseParser.Params;

@RunWith(Parameterized.class)
public class ParseAlternativesTest extends ParseSimpleTest<EmptyConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        return asList( //
                // name, srcPos, content, config, errPos, resultPos, resultText, newSrcPos, errCount
                makeTestData("empty", 0, "", null, 0, 0, null, 0, 2), //
                makeTestData("no match", 0, " flow no", null, 0, 0, null, 0, 2), //
                makeTestData("match flow", 0, "flowabc", null, -1, 0, "flow", 4, 0), //
                makeTestData("match no", 3, "123noabc", null, -1, 3, "no", 5, 0) //
        );
    }

    public ParseAlternativesTest(ParserData parserData, EmptyConfig config, ParseResult expectedResult,
                                 int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, EmptyConfig> makeParser(Params<ParserData> params) {
        ParseLiteral<ParserData> parseLiteralFlow = new ParseLiteral<>(params);
        parseLiteralFlow.getConfigPort().send(new ParseLiteralConfig("flow"));

        ParseLiteral<ParserData> parseLiteralNo = new ParseLiteral<>(params);
        parseLiteralNo.getConfigPort().send(new ParseLiteralConfig("no"));

        ParseAlternatives<ParserData> parseAlternatives = new ParseAlternatives<>(params);
        parseAlternatives.setSubOutPort(0, parseLiteralFlow.getInPort());
        parseAlternatives.setSubOutPort(1, parseLiteralNo.getInPort());
        parseLiteralFlow.setOutPort(parseAlternatives.getSubInPort());
        parseLiteralNo.setOutPort(parseAlternatives.getSubInPort());

        return parseAlternatives;
    }
}
