package com.shangame.fiction.storage.model;


public class DateBean {
    public int year;
    public int mouth;

    public DateBean(int year, int mouth) {
        this.year = year;
        this.mouth = mouth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateBean dateBean = (DateBean) o;
        return year == dateBean.year &&
                mouth == dateBean.mouth;
    }

    @Override
    public String toString() {
        return year+"年"+mouth+"月";
    }

    public String toString2() {
        if(mouth <10){
            return year+"-0"+mouth;
        }else{
            return year+"-"+mouth;
        }
    }
}