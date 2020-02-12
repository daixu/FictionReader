package com.shangame.fiction.net.response;

import java.util.List;

public class AlbumSelectionResponse {
    /**
     * pagesize : 30
     * selectmode : [{"remark":"全部","page":1},{"remark":"1-1770","page":1},{"remark":"17401-1770","page":58},{"remark":"17701-1760","page":59}]
     */

    public int pagesize;
    public List<SelectModeBean> selectmode;

    public static class SelectModeBean {
        /**
         * remark : 全部
         * page : 1
         */

        public String remark;
        public int page;
        public boolean isSelect;
    }
}
