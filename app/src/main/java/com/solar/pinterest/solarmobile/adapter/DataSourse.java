package com.solar.pinterest.solarmobile.adapter;

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

    public ArrayList<String> getUrl() {
        ArrayList<String> urls = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++) {
            urls.add(mData.get(i).imageUrl);
        }
        return urls;
    }

    public ArrayList<String> getTitles() {
        ArrayList<String> titles = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++) {
            titles.add(mData.get(i).title);
        }
        return titles;
    }

    public ArrayList<Integer> getId() {
        ArrayList<Integer> idData = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++) {
            idData.add(mData.get(i).id);
        }
        return idData;
    }

    public void createDataList(ArrayList<String> urls, ArrayList<String> titles, ArrayList<Integer> idData) {
        for (int i = 0; i < urls.size(); i++) {
            mData.add(new DataItem(urls.get(i), titles.get(i), idData.get(i)));
        }
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
