

package asa.org.bd.ammsma.tableview;


import java.util.ArrayList;
import java.util.List;

import asa.org.bd.ammsma.extra.RealizedMemberData;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewModelForRealizedInformationSelectedGroups {

    private String [] values = {"Group Name","Loan Realizable Total","Primary Loan","Secondary Loan","Supplementary Loan","Saving Deposit","Saving Withdrawal"
            ,"CBS Deposit","CBS Withdraw","LTS Deposit","Bad-Debt Collection",
            "Exemption Amount","Net Collection"};
    private List<RealizedMemberData> realizedMemberInformationList ;

    private static final int TOTAL_SINGLE_GROUP = -12345;
    private static final int TOTAL_GRAND_GROUP = -54321;

    public TableViewModelForRealizedInformationSelectedGroups( List<RealizedMemberData> realizedMemberInformationList) {

        this.realizedMemberInformationList = realizedMemberInformationList;

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < realizedMemberInformationList.size(); i++) {

            if (realizedMemberInformationList.get(i).getMemberId() == TOTAL_SINGLE_GROUP || realizedMemberInformationList.get(i).getMemberId() == TOTAL_GRAND_GROUP) {

                RowHeader header = new RowHeader(String.valueOf(i), realizedMemberInformationList.get(i).getMemberName());
                list.add(header);
            } else {

                RowHeader header = new RowHeader(String.valueOf(i), realizedMemberInformationList.get(i).getMemberName() + "/" + realizedMemberInformationList.get(i).getFatherName() + " (" + realizedMemberInformationList.get(i).getPassbookNumber() + ")");
                list.add(header);
            }

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
        for (int i = 0; i < realizedMemberInformationList.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < values.length; j++) {
                String id = j + "-" + i;

                Cell cell ;


                if (j == 0 ) {
                    if(realizedMemberInformationList.get(i).getGroupName()==null)
                    {
                        cell = new Cell(id,"");
                    }
                    else
                    {
                        cell = new Cell(id,realizedMemberInformationList.get(i).getGroupName());
                    }



                }
                else if (j == 1 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getTotalRealizable())) );
                }
                else if(j == 2 ) {
                    cell = new Cell(id,String.valueOf(Math.round( realizedMemberInformationList.get(i).getPrimaryCollection())));
                }
                else if (j == 3) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getSecondaryCollection())) );
                }
                else if(j == 4) {
                    cell = new Cell(id,String.valueOf(Math.round( realizedMemberInformationList.get(i).getSupplementaryCollection())));
                }
                else if (j == 5) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getSavingsDepositWithoutLTS())) );
                }
                else if(j ==6) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getSavingsWithdrawal())) );
                }
                else if (j == 7 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getCbsDeposit())) );
                }
                else if (j == 8 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getCbsWithdrawal())) );
                }
                else if(j ==9) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getLtsDeposit())) );
                }
                else if (j == 10 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getBadDebtCollection())) );
                }
                else if(j == 11) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getExemptionTotal())) );
                }
                else  {
                    cell = new Cell(id,String.valueOf(Math.round(realizedMemberInformationList.get(i).getNetCollection())) );
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
