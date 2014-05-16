package org.flowdev.parser.util;

import org.flowdev.parser.data.SourceData;
import org.junit.Test;

import static org.flowdev.parser.util.ParserUtil.generateWhereMessage;
import static org.junit.Assert.assertEquals;

public class ParserUtilTest {
    @Test
    public void testWhereEmpty() {
        SourceData src = new SourceData();
        src.content = "";
        src.name = "empty";
        src.pos = 0;
        String where = ParserUtil.where(src, 0);
        assertEquals("where didn't work correct for empty source.", generateWhereMessage(src.name, 1, 1, ""), where);
    }

    @Test
    public void testWhereSimple() {
        SourceData src = new SourceData();
        src.content = "simple content\n";
        src.name = "simple";
        src.pos = 0;
        String where = ParserUtil.where(src, 10);
        assertEquals("where didn't work correct for simple source.",
                generateWhereMessage(src.name, 1, 11, src.content.substring(0, 14)), where);
    }

    @Test
    public void testWhereForwardStart() {
        SourceData src = new SourceData();
        src.content = "forward content\nline 2 of forward content\nline 3";
        src.name = "forward start";
        src.pos = 0;
        String where = ParserUtil.where(src, 16);
        assertEquals("where didn't work correct for forward start source.",
                generateWhereMessage(src.name, 2, 1, src.content.substring(16, 41)), where);
    }

    @Test
    public void testWhereForwardEnd() {
        SourceData src = new SourceData();
        src.content = "forward content\nline 2 of forward content\nline 3";
        src.name = "forward end";
        src.pos = 0;
        String where = ParserUtil.where(src, 40);
        assertEquals("where didn't work correct for forward end source.",
                generateWhereMessage(src.name, 2, 25, src.content.substring(16, 41)), where);
    }

    @Test
    public void testWhereBackwardStart() {
        SourceData src = new SourceData();
        src.content = "backward content\nline 2 of backward content\nline 3";
        src.name = "backward start";
        src.pos = 0;
        src.whereLine = 3;
        src.wherePrevNl = 43;
        String where = ParserUtil.where(src, 17);
        assertEquals("where didn't work correct for backward start source.",
                generateWhereMessage(src.name, 2, 1, src.content.substring(17, 43)), where);
    }

    @Test
    public void testWhereBackwardEnd() {
        SourceData src = new SourceData();
        src.content = "backward content\nline 2 of backward content\nline 3";
        src.name = "backward end";
        src.pos = 0;
        String where = ParserUtil.where(src, 42);
        assertEquals("where didn't work correct for backward end source.",
                generateWhereMessage(src.name, 2, 26, src.content.substring(17, 43)), where);
    }
}
