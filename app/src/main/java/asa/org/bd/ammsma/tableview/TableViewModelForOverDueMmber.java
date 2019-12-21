

package asa.org.bd.ammsma.tableview;

import java.util.ArrayList;
import java.util.List;

import asa.org.bd.ammsma.extra.OverDueMember;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewModelForOverDueMmber {

    private String [] values = {"Group Name","Program","Disbursed Date","Disbursed Amount","Outstanding Amount"
            ,"Overdue Amount"};
    private List<OverDueMember> overDueMemberList;

    public TableViewModelForOverDueMmber(List<OverDueMember> overDueMemberList) {

        this.overDueMemberList = overDueMemberList;

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < overDueMemberList.size(); i++) {
            RowHeader header = new RowHeader(String.valueOf(i), overDueMemberList.get(i).getMemberName());
            list.add(header);
        }

        return list;
    }




    /**
     * This is a dummy model list test some cases.
     */
    private List<ColumnHeader> getRandomColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {

            ColumnHeader header = new ColumnHeader(String.valueOf(i), values[i]);
            list.add(header);
        }

        return list;
    }

    private List<List<Cell>> getCellListForSortingTest() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < overDueMemberList.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < values.length; j++) {
                String id = j + "-" + i;

                Cell cell;


                if(j==0)
                {
                    cell = new Cell(id,overDueMemberList.get(i).getGroupName());
                }
                else if(j==1)
                {
                    if(overDueMemberList.get(i).getProgramName()==null)
                    {
                        cell = new Cell(id,"");
                    }
                    else if(overDueMemberList.get(i).isSupplementary())
                    {
                        cell = new Cell(id,overDueMemberList.get(i).getProgramName()+" (S)");
                    }
                    else
                    {
                        cell = new Cell(id,overDueMemberList.get(i).getProgramName());
                    }

                }
                else if(j==2)
                {
                    cell = new Cell(id,overDueMemberList.get(i).getAccountOpeningDate());
                }
                else if(j==3)
                {
                    cell = new Cell(id,Math.round(overDueMemberList.get(i).getDisbursedAmount()));
                }
                else if(j==4)
                {
                    cell = new Cell(id,Math.round(overDueMemberList.get(i).getOutstandingAmount()));
                }
                else
                {
                    cell = new Cell(id,Math.round(overDueMemberList.get(i).getOverDueAmount()));
                }
                cellList.add(cell);


            }
            list.add(cellList);
        }

        return list;
    }


    public List<List<Cell>> getCellList() {
        return getCellListForSortingTest();
    }

    public List<RowHeader> getRowHeaderList() {
        return getSimpleRowHeaderList();
    }

    public List<ColumnHeader> getColumnHeaderList() {
        return getRandomColumnHeaderList();
    }

}
