package org.flowdev.parser.util;

import org.flowdev.parser.data.SourceData;
import org.junit.Test;

import static org.flowdev.parser.util.ParserUtil.generateWhereMessage;
import static org.junit.Assert.assertEquals;

public class ParserUtilTest {
    @Test
    public void testWhereEmpty() {
        SourceData src = new SourceData().content("").name("empty").pos(0);
        String where = ParserUtil.where(src, 0);
        assertEquals("where didn't work correct for empty source.", generateWhereMessage(src.name(), 1, 1, ""), where);
    }

    @Test
    public void testWhereSimple() {
        SourceData src = new SourceData().content("simple content\n").name("simple").pos(0);
        String where = ParserUtil.where(src, 10);
        assertEquals("where didn't work correct for simple source.",
                generateWhereMessage(src.name(), 1, 11, src.content().substring(0, 14)), where);
    }

    @Test
    public void testWhereForwardStart() {
        SourceData src = new SourceData().content("forward content\nline 2 of forward content\nline 3")
                .name("forward start").pos(0);
        String where = ParserUtil.where(src, 16);
        assertEquals("where didn't work correct for forward start source.",
                generateWhereMessage(src.name(), 2, 1, src.content().substring(16, 41)), where);
    }

    @Test
    public void testWhereForwardEnd() {
        SourceData src = new SourceData().content("forward content\nline 2 of forward content\nline 3")
                .name("forward end").pos(0);
        String where = ParserUtil.where(src, 40);
        assertEquals("where didn't work correct for forward end source.",
                generateWhereMessage(src.name(), 2, 25, src.content().substring(16, 41)), where);
    }

    @Test
    public void testWhereBackwardStart() {
        SourceData src = new SourceData().content("backward content\nline 2 of backward content\nline 3")
                .name("backward start").pos(0).whereLine(3).wherePrevNl(43);
        String where = ParserUtil.where(src, 17);
        assertEquals("where didn't work correct for backward start source.",
                generateWhereMessage(src.name(), 2, 1, src.content().substring(17, 43)), where);
    }

    @Test
    public void testWhereBackwardEnd() {
        SourceData src = new SourceData().content("backward content\nline 2 of backward content\nline 3")
                .name("backward end").pos(0);
        String where = ParserUtil.where(src, 42);
        assertEquals("where didn't work correct for backward end source.",
                generateWhereMessage(src.name(), 2, 26, src.content().substring(17, 43)), where);
    }
}
