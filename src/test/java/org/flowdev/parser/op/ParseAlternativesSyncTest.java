package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class ParseAlternativesSyncTest extends ParseSimpleTest<UseTextSemanticConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        UseTextSemanticConfig config = new UseTextSemanticConfig(false);
        return asList( //
                // name, srcPos, content, config, errPos, resultPos, resultText, newSrcPos, errCount
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 2), //
                makeTestData("no match", 0, " flow no", config, 0, 0, null, 0, 2), //
                makeTestData("match flow", 0, "flowabc", config, -1, 0, "flow", 4, 0), //
                makeTestData("match no", 3, "123noabc", config, -1, 3, "no", 5, 0) //
        );
    }

    public ParseAlternativesSyncTest(ParserData parserData, UseTextSemanticConfig config, ParseResult expectedResult,
                                     int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected BaseParser<ParserData, UseTextSemanticConfig> makeParser(ParserParams<ParserData> params) {
        ParseLiteral<ParserData> parseLiteralFlow = new ParseLiteral<>(params);
        parseLiteralFlow.getConfigPort().send(new ParseLiteral.ParseLiteralConfig("flow"));

        ParseLiteral<ParserData> parseLiteralNo = new ParseLiteral<>(params);
        parseLiteralNo.getConfigPort().send(new ParseLiteral.ParseLiteralConfig("no"));

        ParseAlternativesSync<ParserData> parseAlternatives = new ParseAlternativesSync<>(params);
        parseAlternatives.setSubOutPort(0, parseLiteralFlow.getInPort());
        parseAlternatives.setSubOutPort(1, parseLiteralNo.getInPort());
        parseLiteralFlow.setOutPort(parseAlternatives.getSubInPort());
        parseLiteralNo.setOutPort(parseAlternatives.getSubInPort());

        return parseAlternatives;
    }
}
