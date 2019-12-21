

package asa.org.bd.ammsma.tableview;


import java.util.ArrayList;
import java.util.List;
import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewModelForMemberBalance {

    private String [] values = {"Primary Disburse Date","Primary Disbursed","Pr.Out.Ins #"
            ,"Primary Overdue","Primary Outstanding","Secondary Disbursed","Secondary Overdue","Secondary Outstanding",
            "Saving Balance","LTS Balance","CBS Balance","NET Balance"};
    private List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList ;

    public TableViewModelForMemberBalance(List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList) {

        this.miscellaneousMemberBalanceList = miscellaneousMemberBalanceList;

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < miscellaneousMemberBalanceList.size(); i++) {
            RowHeader header = new RowHeader(String.valueOf(i), miscellaneousMemberBalanceList.get(i).getMemberName());
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
        for (int i = 0; i < miscellaneousMemberBalanceList.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < values.length; j++) {
                String id = j + "-" + i;

                Cell cell ;


                if (j == 0 ) {
                    if(miscellaneousMemberBalanceList.get(i).getPrimaryDisbursedDate()==null)
                    {
                        cell = new Cell(id,"");
                    }
                    else
                    {
                        cell = new Cell(id,miscellaneousMemberBalanceList.get(i).getPrimaryDisbursedDate());
                    }


                }
                else if (j == 1 ) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getPrimaryDisbursed())) );
                }
                else if(j == 2 ) {
                    if(i==miscellaneousMemberBalanceList.size()-1)
                    {
                        cell = new Cell(id, "");
                    }
                    else
                    {
                        cell = new Cell(id, String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getPrimaryInstallmentNumber())));
                    }

                }
                else if (j == 3) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getPrimaryOverdue())) );
                }
                else if (j == 4 ) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getPrimaryOutstanding())) );
                }
                else if(j == 5) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getSecondaryDisbursed())) );
                }
                else if (j == 6) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getSecondaryOverdue())) );
                }
                else if(j ==7) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getSecondaryOutstanding())) );
                }

                else if(j ==8) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getSavingsBalance())) );
                }
                else if (j == 9 ) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getLtsBalance() )));
                }
                else if(j == 10) {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getCbsBalance())) );
                }
                else  {
                    cell = new Cell(id,String.valueOf(Math.round(miscellaneousMemberBalanceList.get(i).getNetBalance())) );
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
