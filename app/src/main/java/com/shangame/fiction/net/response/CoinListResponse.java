package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/12/29
 */
public class CoinListResponse {
    public int  records;
    public int  total;
    public List<CoinSummaryResponse.CoinItem> pagedata;
}
