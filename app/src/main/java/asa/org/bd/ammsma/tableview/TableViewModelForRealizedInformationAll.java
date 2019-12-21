/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package asa.org.bd.ammsma.tableview;

import java.util.ArrayList;
import java.util.List;

import asa.org.bd.ammsma.extra.RealizedGroupData;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewModelForRealizedInformationAll {

    private String [] values = {"Loan Realizable Total","Loan Collection","Savings Deposit","Savings Withdrawal","CBS Deposit","CBS Withdrawal","LTS Deposit"
            ,"BadDebt Collection","Exemption Amount","Net Collection"};
    private List<RealizedGroupData> realizedGroupDataArrayList ;

    public TableViewModelForRealizedInformationAll(List<RealizedGroupData> realizedGroupDataArrayList) {

        this.realizedGroupDataArrayList = realizedGroupDataArrayList;

    }

    private List<RowHeader> getSimpleRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < realizedGroupDataArrayList.size(); i++) {


            if (realizedGroupDataArrayList.get(i).getGroupId() == (-12345)) {
                RowHeader header = new RowHeader(String.valueOf(i), realizedGroupDataArrayList.get(i).getGroupName());
                list.add(header);

            } else {
                RowHeader header = new RowHeader(String.valueOf(i), realizedGroupDataArrayList.get(i).getGroupName() + " (" + titleCase(realizedGroupDataArrayList.get(i).getMeetingDay()) + ")");
                list.add(header);
            }

        }

        return list;
    }


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
        for (int i = 0; i < realizedGroupDataArrayList.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < values.length; j++) {
                String id = j + "-" + i;

                Cell cell = null;


                if (j == 0 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getLoanRealizable())));
                }
                else if (j == 1 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getLoanCollection())));

                }
                else if (j == 2 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getSavingsDepositWithoutLts())) );
                }
                else if(j == 3 ) {
                    cell = new Cell(id, String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getSavingsWithdrawal())));
                }
                else if (j == 4) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getCbsDeposit())));
                }
                else if(j == 5) {
                    cell = new Cell(id, String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getCbsWithdrawal())));
                }
                else if (j == 6) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getLtsDeposit())));
                }
                else if(j ==7) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getBadDebtCollection())));
                }
                else if(j ==8) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getExemptionTotal())));
                }
                else if (j == 9 ) {
                    cell = new Cell(id,String.valueOf(Math.round(realizedGroupDataArrayList.get(i).getNetCollection())));
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
    private static String titleCase(String givenString) {

        if (givenString.trim().contains(" ")) {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            return stringBuffer.toString().trim();
        } else {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }

}
