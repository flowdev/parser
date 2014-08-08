package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.flowdev.parser.data.UseTextSemanticConfig;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseLiteral.ParseLiteralConfig;

@RunWith(Parameterized.class)
public class ParseAllSyncTest extends ParseSimpleTest<UseTextSemanticConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        UseTextSemanticConfig config = new UseTextSemanticConfig(true);
        return asList( //
                // name, srcPos, content, config, errPos, resultPos, resultText, newSrcPos, errCount
                makeTestData("empty", 0, "", config, 0, 0, null, 0, 1), //
                makeTestData("no match", 0, " flow no", config, 0, 0, null, 0, 1), //
                makeTestData("match flow", 0, "flowabc", config, 4, 0, null, 0, 1), //
                makeTestData("match no", 3, "123noabc", config, 3, 0, null, 3, 1), //
                makeTestData("match all", 3, "123flownoabc", config, -1, 3, "flowno", 9, 0) //
        );
    }

    public ParseAllSyncTest(ParserData parserData, UseTextSemanticConfig config, ParseResult expectedResult,
                            int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected BaseParser<ParserData, UseTextSemanticConfig> makeParser(ParserParams<ParserData> params) {
        ParseLiteral<ParserData> parseLiteralFlow = new ParseLiteral<>(params);
        parseLiteralFlow.getConfigPort().send(new ParseLiteralConfig("flow"));

        ParseLiteral<ParserData> parseLiteralNo = new ParseLiteral<>(params);
        parseLiteralNo.getConfigPort().send(new ParseLiteralConfig("no"));

        ParseAllSync<ParserData> parseAll = new ParseAllSync<>(params);
        parseAll.setSubOutPort(0, parseLiteralFlow.getInPort());
        parseAll.setSubOutPort(1, parseLiteralNo.getInPort());
        parseLiteralFlow.setOutPort(parseAll.getSubInPort());
        parseLiteralNo.setOutPort(parseAll.getSubInPort());

        return parseAll;
    }
}
