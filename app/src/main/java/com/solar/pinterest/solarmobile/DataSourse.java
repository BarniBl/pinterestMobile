package com.solar.pinterest.solarmobile;

import java.util.ArrayList;
import java.util.List;

public class DataSourse {

    private final List<DataItem> mData;

    public DataSourse() {
        mData = new ArrayList<>();
    }

    public void addDataItem(String nImageUrl, String nTitle, int nId) {
        mData.add(new DataItem(nImageUrl, nTitle, nId));
    }

    public List<DataItem> getDataSource() {
        return mData;
    }

    public int getDataSize() {
        return mData.size();
    }

    public static class DataItem {

        String imageUrl;
        String title;
        int id;

        public DataItem(String nImageUrl, String nTitle, int nId) {
            imageUrl = nImageUrl;
            title = nTitle;
            id = nId;
        }
    }
}
