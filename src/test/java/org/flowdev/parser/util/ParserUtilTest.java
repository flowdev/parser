package org.flowdev.parser.util;

import org.flowdev.parser.data.SourceData;
import org.junit.Test;

import static org.flowdev.parser.util.ParserUtil.generateWhereMessage;
import static org.junit.Assert.assertEquals;

public class ParserUtilTest {
    @Test
    public void testWhereEmpty() {
        SourceData src = new SourceData();
        src.setContent("");
        src.setName("empty");
        src.setPos(0);
        String where = ParserUtil.where(src, 0);
        assertEquals("where didn't work correct for empty source.", generateWhereMessage(src.getName(), 1, 1, ""), where);
    }

    @Test
    public void testWhereSimple() {
        SourceData src = new SourceData();
        src.setContent("simple content\n");
        src.setName("simple");
        src.setPos(0);
        String where = ParserUtil.where(src, 10);
        assertEquals("where didn't work correct for simple source.",
                generateWhereMessage(src.getName(), 1, 11, src.getContent().substring(0, 14)), where);
    }

    @Test
    public void testWhereForwardStart() {
        SourceData src = new SourceData();
        src.setContent("forward content\nline 2 of forward content\nline 3");
        src.setName("forward start");
        src.setPos(0);
        String where = ParserUtil.where(src, 16);
        assertEquals("where didn't work correct for forward start source.",
                generateWhereMessage(src.getName(), 2, 1, src.getContent().substring(16, 41)), where);
    }

    @Test
    public void testWhereForwardEnd() {
        SourceData src = new SourceData();
        src.setContent("forward content\nline 2 of forward content\nline 3");
        src.setName("forward end");
        src.setPos(0);
        String where = ParserUtil.where(src, 40);
        assertEquals("where didn't work correct for forward end source.",
                generateWhereMessage(src.getName(), 2, 25, src.getContent().substring(16, 41)), where);
    }

    @Test
    public void testWhereBackwardStart() {
        SourceData src = new SourceData();
        src.setContent("backward content\nline 2 of backward content\nline 3");
        src.setName("backward start");
        src.setPos(0);
        src.setWhereLine(3);
        src.setWherePrevNl(43);
        String where = ParserUtil.where(src, 17);
        assertEquals("where didn't work correct for backward start source.",
                generateWhereMessage(src.getName(), 2, 1, src.getContent().substring(17, 43)), where);
    }

    @Test
    public void testWhereBackwardEnd() {
        SourceData src = new SourceData();
        src.setContent("backward content\nline 2 of backward content\nline 3");
        src.setName("backward end");
        src.setPos(0);
        String where = ParserUtil.where(src, 42);
        assertEquals("where didn't work correct for backward end source.",
                generateWhereMessage(src.getName(), 2, 26, src.getContent().substring(17, 43)), where);
    }
}
