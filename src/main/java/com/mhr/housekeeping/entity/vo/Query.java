package com.mhr.housekeeping.entity.vo;


import com.mhr.housekeeping.utils.DataUtils;

import java.util.HashMap;
import java.util.Map;

public class Query {
    //排序字段，默认按照id进行排序
    public Map sortByParams = new HashMap();

    {
        sortByParams.put("id", "ASC");
    }

   /* private int size;//
    private int page;//当前的页码

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }*/

    public Map getSortByParams() {
        return sortByParams;
    }

    public void setSortByParams(Map sortByParams) {
        this.sortByParams = sortByParams;
    }


}
