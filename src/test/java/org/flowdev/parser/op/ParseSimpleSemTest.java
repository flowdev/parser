package org.flowdev.parser.op;

import org.flowdev.base.data.NoConfig;
import org.flowdev.base.op.FilterOp;
import org.flowdev.parser.data.ParseResult;
import org.flowdev.parser.data.ParserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.flowdev.parser.op.ParseLiteral.ParseLiteralConfig;
import static org.flowdev.parser.util.ParserUtil.matched;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class ParseSimpleSemTest extends ParseSimpleTest<ParseLiteralConfig> {
    private static final String EXPECTED_SEM_OBJECT = "SemTestData!";

    @Parameterized.Parameters
    public static Collection<?> generateTestDatas() {
        ParseLiteralConfig config = new ParseLiteralConfig().literal("flow");
        return asList( //
                makeTestData("no match", 0, " flow", config, 0, 0, null, 0, 1), //
                makeTestData("match", 0, "flow", config, -1, 0, "flow", 4, 0) //
        );
    }

    public ParseSimpleSemTest(ParserData parserData, ParseLiteralConfig config, ParseResult expectedResult,
                              int expectedSrcPos, int expectedErrorCount) {
        super(parserData, config, expectedResult, expectedSrcPos, expectedErrorCount);
        dontRunTests = true;
    }

    @Override
    protected ParseSimple<ParserData, ParseLiteralConfig> makeParser(ParserParams<ParserData> params) {
        ParseLiteral<ParserData> parseLiteral = new ParseLiteral<>(params);
        FilterOp<ParserData, NoConfig> semantics = new FilterOp<ParserData, NoConfig>() {
            @Override
            protected void filter(ParserData data) {
                data.result().value(EXPECTED_SEM_OBJECT);
                outPort.send(data);
            }
        };
        parseLiteral.setSemOutPort(semantics.getInPort());
        semantics.setOutPort(parseLiteral.getSemInPort());
        return parseLiteral;
    }

    @Test
    public void testParserSemantics() {
        dontRunTests = false;
        testParser();
        dontRunTests = true;
        if (matched(parserData.result())) {
            assertEquals("Parser did match: Semantics should have been called:", EXPECTED_SEM_OBJECT, parserData.result().value());
        } else {
            assertNull("Parser didn't match: Semantics shouldn't have been called:", parserData.result().value());
        }
    }
}
