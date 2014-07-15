package org.flowdev.parser.op;

import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.BaseParser.Params;

@RunWith(Parameterized.class)
public class ParseMultipleTest extends ParseSimpleTest<ParseMultiple.ParseMultipleConfig> {

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseMultiple.ParseMultipleConfig config0_1 = new ParseMultiple.ParseMultipleConfig(0, 1);
        ParseMultiple.ParseMultipleConfig config0_n = new ParseMultiple.ParseMultipleConfig(0, Integer.MAX_VALUE);
        ParseMultiple.ParseMultipleConfig config1_n = new ParseMultiple.ParseMultipleConfig(1, Integer.MAX_VALUE);
        ParseMultiple.ParseMultipleConfig config2_3 = new ParseMultiple.ParseMultipleConfig(2, 3);
        return asList( //
                // name, srcPos, content, config, errPos, resultPos, resultText, newSrcPos, errCount
                makeTestData("0-1: no match", 0, " flow", config0_1, -1, 0, "", 0, 0), //
                makeTestData("0-1: 1 match", 0, "flow", config0_1, -1, 0, "flow", 4, 0), //
                makeTestData("0-1: 1 match 2", 0, "flowflow", config0_1, -1, 0, "flow", 4, 0), //
                makeTestData("0-n: no match", 0, " flow", config0_n, -1, 0, "", 0, 0), //
                makeTestData("0-n: 1 match", 0, "flow", config0_n, -1, 0, "flow", 4, 0), //
                makeTestData("0-n: 2 match", 0, "flowflow", config0_n, -1, 0, "flowflow", 8, 0), //
                makeTestData("1-n: no match", 0, " flow", config1_n, 0, 0, null, 0, 1), //
                makeTestData("1-n: 1 match", 0, "flow", config1_n, -1, 0, "flow", 4, 0), //
                makeTestData("1-n: 2 match", 0, "flowflow", config1_n, -1, 0, "flowflow", 8, 0), //
                makeTestData("1-n: 5 match", 0, "flowflowflowflowflow", config1_n, -1, 0, "flowflowflowflowflow", 20, 0), //
                makeTestData("2-3: 1 match", 0, "flow flow", config2_3, 4, 0, null, 0, 1), //
                makeTestData("2-3: 2 match", 0, "flowflow", config2_3, -1, 0, "flowflow", 8, 0), //
                makeTestData("2-3: 3 match", 0, "flowflowflowabc", config2_3, -1, 0, "flowflowflow", 12, 0), //
                makeTestData("2-3: 3 match 2", 0, "flowflowflowflow", config2_3, -1, 0, "flowflowflow", 12, 0) //
        );
    }

    public ParseMultipleTest(ParserData parserData, ParseMultiple.ParseMultipleConfig config, ParseResult expectedResult,
                             int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
    }

    @Override
    protected ParseSimple<ParserData, ParseMultiple.ParseMultipleConfig> makeParser(Params<ParserData> params) {
        ParseLiteral.ParseLiteralConfig literalConfig = new ParseLiteral.ParseLiteralConfig("flow");
        ParseLiteral<ParserData> parseLiteral = new ParseLiteral<>(params);
        parseLiteral.getConfigPort().send(literalConfig);

        ParseMultiple<ParserData> parseMultiple = new ParseMultiple<>(params);
        parseMultiple.setSubOutPort(parseLiteral.getInPort());
        parseLiteral.setOutPort(parseMultiple.getSubInPort());
        return parseMultiple;
    }
}
