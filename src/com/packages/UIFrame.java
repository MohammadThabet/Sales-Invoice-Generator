package com.packages;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.event.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class UIFrame extends JFrame {


    public static class GlassView extends JFrame implements TableModelListener, ActionListener
    {


        JButton saveButton = new JButton("Save");
       private final JTable rightTable;
        int invoiceItemsPtr = 0;
        int invoiceDataPtr = 0;
        int currentInvoice = 1;
        public GlassView() {
            getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
            int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);

            JMenuBar menuBar;
            JMenu fileMenu;
            JMenuItem loadItem;
            JMenuItem saveItem;

            String[][] tableData =
                    {
                            {"", "", "", ""},
                            {"", "", "", ""},
                            {"", "", "", ""},
                            {"", "", "", ""}
                    };
            String[] tableColumn = {"No", "Date", "Customer", "Total"};


            String[][] tableData1 =
                    {
                            {"", "", "", "", ""},
                            {"", "", "", "", ""},
                            {"", "", "", "", ""},
                            {"", "", "", "", ""}
                    };
            String[] tableColumn1 = {"No", "Item Name", "Item Price", "Count", "Item Total"};


            List invoiceItemsList = new List();
            List invoiceDataList = new List();
            java.util.List<Item> invoiceItemsList_1 = new ArrayList<>();
            java.util.List<Invoice> invoiceDataList_1 = new ArrayList<>();


            JSplitPane splitPane = new JSplitPane();

            JPanel leftPanel = new JPanel();
            leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    "Invoices Table",
                    TitledBorder.LEFT,
                    TitledBorder.TOP));

            JTable leftTable = new JTable(tableData, tableColumn);
            JScrollPane jScrollPane = new JScrollPane(leftTable);

            JButton createButton = new JButton("Create New Invoice");
            JButton deleteButton = new JButton("Delete Invoice");

            JPanel rightPanel = new JPanel(new GridLayout(3, 1));

            JPanel panel1 = new JPanel(new GridLayout(4, 2, 20, 20));
            JPanel panel2 = new JPanel(new GridLayout(1, 1));
            panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    "Invoice Items",
                    TitledBorder.LEFT,
                    TitledBorder.TOP));
            JPanel panel3 = new JPanel(new FlowLayout());
            panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));

             rightTable = new JTable(tableData1, tableColumn1);
            JScrollPane jScrollPane_1 = new JScrollPane(rightTable);

            JButton cancelButton = new JButton("Cancel");
            JLabel label1 = new JLabel("Invoice Number");
            JLabel label2 = new JLabel("Invoice Date");
            JLabel label3 = new JLabel("Customer Name");
            JLabel label4 = new JLabel("Invoice Total");
            JLabel label5 = new JLabel("1");
            JTextField textField1 = new JTextField();
            JTextField textField2 = new JTextField();
            JLabel totalPrice = new JLabel("0.00");


            leftTable.getSelectionModel().addListSelectionListener(e -> {
                if(!e.getValueIsAdjusting()){
                    currentInvoice = Integer.parseInt(label5.getText());
                    int colIndex = 0;
                    int rowIndex = leftTable.getSelectedRow();
                    String invoiceIdSt = (String)leftTable.getValueAt(rowIndex,colIndex);

                    if(invoiceIdSt.equals(""))return;
                    int invoiceId = Integer.parseInt(invoiceIdSt);


                    //RESET RIGHT TABLE
                    for (int row=0; row<rightTable.getRowCount(); row++) {
                        for (int column = 0; column < rightTable.getColumnCount(); column++) {
                            rightTable.setValueAt("",row,column);
                        }
                    }

                    int tableRow = 0;
                    for(Item item : invoiceItemsList_1){
                        if(item.invoiceId == invoiceId){
                            rightTable.setValueAt(String.valueOf(item.itemId),tableRow,0);
                            rightTable.setValueAt(item.itemName,tableRow,1);
                            rightTable.setValueAt(String.valueOf(item.itemPrice),tableRow,2);
                            rightTable.setValueAt(String.valueOf(item.itemCount),tableRow,3);
                            tableRow++;
                        }
                    }



                    //INSERT DATA TEXT FIELDS
                    label5.setText((String)leftTable.getValueAt(rowIndex,0));
                    textField1.setText((String)leftTable.getValueAt(rowIndex,1));
                    textField2.setText((String)leftTable.getValueAt(rowIndex,2));
                    totalPrice.setText((String)leftTable.getValueAt(rowIndex,3));

                }

            });

            rightTable.getModel().addTableModelListener(e -> {

                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 2 || column == 3) {
                    String price = (String) rightTable.getValueAt(row, 2);
                    if (price.equals("")) return;
                    String count = (String) rightTable.getValueAt(row, 3);
                    if (count.equals("")) return;
                    double convertedPrice = Double.parseDouble(price);
                    double convertedCount = Double.parseDouble(count);
                    double total = convertedPrice * convertedCount;
                    rightTable.setValueAt(String.valueOf(total), row, 4);

                    double totalVal = Double.parseDouble(totalPrice.getText());
                    totalVal = totalVal + total;
                    totalPrice.setText(String.valueOf(totalVal));
                }

            });

            deleteButton.addActionListener(e -> {
                int rowIndex = leftTable.getSelectedRow();
          //      int colIndex = 0;
                int invoiceId = Integer.parseInt(label5.getText());

                //RESET RIGHT PANEL
                textField1.setText("");
                textField2.setText("");
                totalPrice.setText("0.00");
                for (int row=0; row<rightTable.getRowCount(); row++) {
                    for (int column = 0; column < rightTable.getColumnCount(); column++) {
                        rightTable.setValueAt("",row,column);
                    }
                }

                //CLEAR DATA FROM LEFT TABLE BASED ON ID
               leftTable.setValueAt("",rowIndex,0);
                leftTable.setValueAt("",rowIndex,1);
                leftTable.setValueAt("",rowIndex,2);
                leftTable.setValueAt("",rowIndex,3);

                //CLEAR DATA FROM, INVOICE ITEMS List BASED ON ID
                java.util.List<Item> toRemoveItems = new ArrayList<>();
                for(Item item: invoiceItemsList_1){
                    if(item.invoiceId == invoiceId){
                        toRemoveItems.add(item);
                    }
                }
                invoiceItemsList_1.removeAll(toRemoveItems);

                //CLEAR DATA FROM INVOICE DATA List BASED ON ID
                Invoice removedItem = new Invoice();
                for(Invoice invoice : invoiceDataList_1){
                    if(invoice.invoiceId == invoiceId)
                        removedItem = invoice;
                }
                invoiceDataList_1.remove(removedItem);

            });

            createButton.addActionListener(e -> {

                //INSERT ITEMS INTO INVOICE ITEMS LIST
                for (int row=0; row<rightTable.getRowCount(); row++) {
                    if (rightTable.getValueAt(row, 0) != "") {
                        int invoiceId = Integer.parseInt(label5.getText());
                        int itemId = Integer.parseInt((String)rightTable.getValueAt(row,0));
                        String itemName = (String)rightTable.getValueAt(row,1);
                        double itemPrice = Double.parseDouble((String)rightTable.getValueAt(row,2));
                        double itemCount = Double.parseDouble((String)rightTable.getValueAt(row,3));
                        double itemTotal = Double.parseDouble((String)rightTable.getValueAt(row,4));
                        Item item = new Item(invoiceId,itemId,itemName,itemPrice,itemCount,itemTotal);
                        invoiceItemsList_1.add(item);
                    }
                }

                int invoiceId = Integer.parseInt(label5.getText());
                String date = textField1.getText();
                String customerName = textField2.getText();
                double total = Double.parseDouble(totalPrice.getText());
                Invoice invoice = new Invoice(invoiceId,date,customerName,total);
                invoiceDataList_1.add(invoice);


                //UPDATE LEFT TABLE
                leftTable.setValueAt(label5.getText(),invoiceDataPtr,0);
                leftTable.setValueAt(textField1.getText(),invoiceDataPtr,1);
                leftTable.setValueAt(textField2.getText(),invoiceDataPtr,2);
                leftTable.setValueAt(totalPrice.getText(),invoiceDataPtr,3);

                //INCREMENT INVOICE DATA POINTER
                invoiceDataPtr++;

                //INCREMENT INVOICE NUMBER
                int num = Integer.parseInt(label5.getText());
                num++;
                label5.setText(String.valueOf(num));


                //RESET RIGHT TABLE AND ALL FIELDS
                textField1.setText("");
                textField2.setText("");
                totalPrice.setText("0.00");
                for (int row=0; row<rightTable.getRowCount(); row++) {
                    for (int column = 0; column < rightTable.getColumnCount(); column++) {
                        rightTable.setValueAt("",row,column);
                    }
                }


            });

            saveButton.addActionListener(e -> {
                Invoice updatedInvoice = new Invoice();
                int rowIndex = leftTable.getSelectedRow();
                int invoiceId = Integer.parseInt(label5.getText());
                //FETCH UPDATED ITEM
                for(Invoice invoice:invoiceDataList_1){
                    if(invoice.invoiceId == invoiceId){
                        updatedInvoice = invoice;
                        break;
                    }
                }
                //ONCE ITEM FETCHED UPDATE THAT ITEM
                updatedInvoice.date=textField1.getText();
                updatedInvoice.customerName=textField2.getText();
                updatedInvoice.total=Double.parseDouble(totalPrice.getText());

                //UPDATE LEFT TABLE
                leftTable.setValueAt(updatedInvoice.date,rowIndex,1);
                leftTable.setValueAt(updatedInvoice.customerName,rowIndex,2);
                leftTable.setValueAt(String.valueOf(updatedInvoice.total),rowIndex,3);

                //DELETE ALL ITEMS RELATED TO THIS INVOICE ID
                java.util.List<Item> toRemoveItems = new ArrayList<>();
                for(Item item: invoiceItemsList_1){
                    if(item.invoiceId == invoiceId){
                        toRemoveItems.add(item);
                    }
                }
                invoiceItemsList_1.removeAll(toRemoveItems);

                //INSERT NEW ITEMS THIS INVOICE
                for (int row=0; row<rightTable.getRowCount(); row++) {
                    if (rightTable.getValueAt(row, 0) != "") {
                        int itemId = Integer.parseInt((String)rightTable.getValueAt(row,0));
                        String itemName = (String)rightTable.getValueAt(row,1);
                        double itemPrice = Double.parseDouble((String)rightTable.getValueAt(row,2));
                        double itemCount = Double.parseDouble((String)rightTable.getValueAt(row,3));
                        double itemTotal = Double.parseDouble((String)rightTable.getValueAt(row,4));
                        Item item = new Item(invoiceId,itemId,itemName,itemPrice,itemCount,itemTotal);
                        invoiceItemsList_1.add(item);
                    }
                }
            });

            cancelButton.addActionListener(e -> {

                //RESET RIGHT PANEL
                label5.setText(String.valueOf(currentInvoice));
                textField1.setText("");
                textField2.setText("");
                totalPrice.setText("0.00");
                for (int row=0; row<rightTable.getRowCount(); row++) {
                    for (int column = 0; column < rightTable.getColumnCount(); column++) {
                        rightTable.setValueAt("",row,column);
                    }
                }

            });


            int width = 1000;
            int height = 560;
            setPreferredSize(new Dimension(width, height));
            getContentPane().setLayout(new GridLayout());
            getContentPane().add(splitPane);
            splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(width / 2);
            splitPane.setLeftComponent(leftPanel);
            splitPane.setRightComponent(rightPanel);

            leftPanel.add(jScrollPane);
            leftPanel.add(createButton);
            leftPanel.add(deleteButton);

            rightPanel.add(panel1);
            rightPanel.add(panel2);
            rightPanel.add(panel3);

            panel1.add(label1);
            panel1.add(label5);
            panel1.add(label2);
            panel1.add(textField1);
            panel1.add(label3);
            panel1.add(textField2);
            panel1.add(label4);
            panel1.add(totalPrice);

            panel2.add(jScrollPane_1);

            panel3.add(saveButton);
            panel3.add(cancelButton);

            pack();

            menuBar = new JMenuBar();
            loadItem = new JMenuItem("Load File", 'L');

            loadItem.addActionListener(e -> {

                //IMPORT INVOICE DATA
                importInvoiceData(invoiceDataList_1,leftTable,GlassView.this);
                //IMPORT INVOICE ITEMS
                importInvoiceItems(invoiceItemsList_1,GlassView.this);

            });

            loadItem.setActionCommand("L");
            saveItem = new JMenuItem("Save File", 'S');
            saveItem.addActionListener(e -> {
                exportInvoiceItems(invoiceItemsList_1,GlassView.this);
                exportInvoiceData(invoiceDataList_1,GlassView.this);
            });
            saveItem.setActionCommand("S");
            fileMenu = new JMenu("File");
            fileMenu.add(loadItem);
            fileMenu.addSeparator();
            fileMenu.add(saveItem);
            menuBar.add(fileMenu);
            setJMenuBar(menuBar);

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setLocation(x - width / 2, y - height / 2);
            this.setVisible(true);
            this.setSize(width, height);


        }


        @Override
        public void tableChanged(TableModelEvent e) {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        }


    public static void exportInvoiceData (java.util.List<Invoice> invoiceData,GlassView gv){
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(gv);
        if(option == JFileChooser.APPROVE_OPTION){
            //SELECT FILE PATH
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";

            //CREATE FILE
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("Invoice num" + "\t");
                fw.write("Date" + "\t");
                fw.write("Customer Name" + "\t");
                fw.write("Total" + "\t");
                fw.write("\n");

                //INSERT ITEMS INTO INVOICE ITEMS ARRAY TEST
                for(Invoice invoice:invoiceData){
                    fw.write(invoice.invoiceId + "\t");
                    fw.write(invoice.date + "\t");
                    fw.write(invoice.customerName + "\t");
                    fw.write(invoice.total + "\t");
                    fw.write("\n");
                }

                fw.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static void importInvoiceItems(java.util.List<Item> invoiceItems, GlassView gv){
        try{
            JFileChooser fchoose = new JFileChooser();
            int option = fchoose.showOpenDialog(gv);
            if(option == JFileChooser.APPROVE_OPTION){
                //SELECT FILE PATH
                File selectedFile = fchoose.getSelectedFile();
                String filePath =selectedFile.getAbsolutePath();
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook wb=new XSSFWorkbook(fis);
                XSSFSheet sheet=wb.getSheetAt(0);
                FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();
                String [] invoiceItemsArray = new String[6];
                for(Row row: sheet)
                {
                    if(row.getRowNum() == 0)continue;
                    Item item = new Item();
                    for(Cell cell: row){
                        CellType c = formulaEvaluator.evaluateInCell(cell).getCellType();
                        if(formulaEvaluator.evaluateInCell(cell).getCellType().name().equals("STRING")){
                            String st = cell.getStringCellValue();
                            invoiceItemsArray[cell.getColumnIndex()] = st;
                        }
                        else{

                            String st = String.valueOf(cell.getNumericCellValue());
                            invoiceItemsArray[cell.getColumnIndex()] = st;
                        }
                    }

                    item.invoiceId = (int) Double.parseDouble(invoiceItemsArray[0]);
                    item.itemId = (int) Double.parseDouble(invoiceItemsArray[1]);
                    item.itemName = invoiceItemsArray[2];
                    item.itemPrice = Double.parseDouble(invoiceItemsArray[3]);
                    item.itemCount = Double.parseDouble(invoiceItemsArray[4]);
                    item.total = Double.parseDouble(invoiceItemsArray[5]);
                    invoiceItems.add(item);
                }

            }
        }
        catch (Exception ignored){}
    }


    public static void importInvoiceData(java.util.List<Invoice> invoiceDataList ,JTable table, GlassView gv){
        try{
            JFileChooser fchoose = new JFileChooser();
            int option = fchoose.showOpenDialog(gv);
            if(option == JFileChooser.APPROVE_OPTION){
                //FIRST IMPORT IS INVOICE DATA

                File selectedFile = fchoose.getSelectedFile();
                String filePath =selectedFile.getAbsolutePath();
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook wb=new XSSFWorkbook(fis);
                XSSFSheet sheet=wb.getSheetAt(0);
                FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();
                String [] invoiceData = new String[4];
                for(Row row: sheet)
                {
                    if(row.getRowNum() == 0)continue;
                    Invoice invoice = new Invoice();
                    for(Cell cell: row){
                        CellType c = formulaEvaluator.evaluateInCell(cell).getCellType();
                        if(formulaEvaluator.evaluateInCell(cell).getCellType().name().equals("STRING")){
                            String st = cell.getStringCellValue();
                            invoiceData[cell.getColumnIndex()] = st;
                        }
                        else{

                            String st = String.valueOf(cell.getNumericCellValue());
                            invoiceData[cell.getColumnIndex()] = st;
                        }
                    }

                    invoice.invoiceId = (int) Double.parseDouble(invoiceData[0]);
                    invoice.date = invoiceData[1];
                    invoice.customerName = invoiceData[2];
                    invoice.total = Double.parseDouble(invoiceData[3]);
                    invoiceDataList.add(invoice);
                }
                int rowId =0;
                for(Invoice invoice:invoiceDataList){
                    table.setValueAt(String.valueOf(invoice.invoiceId),rowId,0);
                    table.setValueAt(invoice.date,rowId,1);
                    table.setValueAt(invoice.customerName,rowId,2);
                    table.setValueAt(String.valueOf(invoice.total),rowId,3);
                    rowId++;
                }
            }
        }
        catch (Exception ignored){}
    }
    public static void exportInvoiceItems(java.util.List<Item> invoiceItems,GlassView gv){
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(gv);
        if(option == JFileChooser.APPROVE_OPTION){
            //SELECT FILE PATH
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";

            //CREATE FILE
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("Invoice num" + "\t");
                fw.write("Item num" + "\t");
                fw.write("item Name" + "\t");
                fw.write("Item price" + "\t");
                fw.write("Item count" + "\t");
                fw.write("Total" + "\t");
                fw.write("\n");

                //INSERT ITEMS INTO INVOICE ITEMS ARRAY TEST
                for(Item item:invoiceItems){
                    fw.write(item.invoiceId + "\t");
                    fw.write(item.itemId + "\t");
                    fw.write(item.itemName + "\t");
                    fw.write(item.itemPrice + "\t");
                    fw.write(item.itemCount + "\t");
                    fw.write(item.total + "\t");
                    fw.write("\n");
                }

                fw.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
