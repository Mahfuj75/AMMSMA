

package asa.org.bd.ammsma.tableview.model;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;



public class Cell implements ISortableModel, IFilterableModel {

    private String mId;
    private Object mData;
    private String mFilterKeyword;

    Cell(String id) {
        this.mId = id;
    }

    public Cell(String id, Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
    }


    @Override
    public String getId() {
        return mId;
    }


    @Override
    public Object getContent() {
        return mData;
    }


    public Object getData() {
        return mData;
    }

    public void setData(String data) { mData = data; }

    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }
}

