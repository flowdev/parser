package org.flowdev.parser.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ole on 1/12/14.
 */
public class ParserTempData {
    public int orgSrcPos;
    public int index;
    public List<ParseResult> subResults;

    public ParserTempData(int orgSrcPos) {
        this.orgSrcPos = orgSrcPos;
        this.index = 0;
        this.subResults = new ArrayList<>(128);
    }
}
