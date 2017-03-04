package ru.sergst.aminoasidscore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    TextField C1;
    @FXML
    TextField C2;
    @FXML
    TextField C3;
    @FXML
    TextField C4;
    @FXML
    TextField C5;
    @FXML
    TextField C6;
    @FXML
    TextField C7;
    @FXML
    TextField C8;
    @FXML
    TextField B1;
    @FXML
    TextField B2;
    @FXML
    TextField B3;
    @FXML
    TextField B4;
    @FXML
    TextField B5;
    @FXML
    TextField B6;
    @FXML
    TextField B7;
    @FXML
    TextField B8;

    @FXML
    LineChart<Number, Number> lineChart;

    /**
     * Эталонные зачения
     */
    private final int[] Ae = {50,70,40,55,35,40,10,60};

    private Main main;
    void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void handleCalculate() {
        printInput();
        double[] c = new double[8];
        double[] b = new double[8];
        List<ResultRow> result;
        if (isInputValid()) {
            c[0] = Double.parseDouble(C1.getText().replaceAll(",", "."));
            c[1] = Double.parseDouble(C2.getText().replaceAll(",", "."));
            c[2] = Double.parseDouble(C3.getText().replaceAll(",", "."));
            c[3] = Double.parseDouble(C4.getText().replaceAll(",", "."));
            c[4] = Double.parseDouble(C5.getText().replaceAll(",", "."));
            c[5] = Double.parseDouble(C6.getText().replaceAll(",", "."));
            c[6] = Double.parseDouble(C7.getText().replaceAll(",", "."));
            c[7] = Double.parseDouble(C8.getText().replaceAll(",", "."));
            b[0] = Double.parseDouble(B1.getText().replaceAll(",", "."));
            b[1] = Double.parseDouble(B2.getText().replaceAll(",", "."));
            b[2] = Double.parseDouble(B3.getText().replaceAll(",", "."));
            b[3] = Double.parseDouble(B4.getText().replaceAll(",", "."));
            b[4] = Double.parseDouble(B5.getText().replaceAll(",", "."));
            b[5] = Double.parseDouble(B6.getText().replaceAll(",", "."));
            b[6] = Double.parseDouble(B7.getText().replaceAll(",", "."));
            b[7] = Double.parseDouble(B8.getText().replaceAll(",", "."));
            result = calcScore(c, b);
            printResult(result);
            String name = exportResultToXLSX(result, c, b);
            renderLineChart(result, c, b, name);
        } else {
            System.out.println("Invalid input");
        }

    }

    private boolean isInputValid() {
        Pattern pattern = Pattern.compile("\\d+[,\\.]?(\\d+)?");
        return
                StringUtils.isNotBlank(C1.getText()) && pattern.matcher(C1.getText()).matches() &&
                StringUtils.isNotBlank(C2.getText()) && pattern.matcher(C2.getText()).matches() &&
                StringUtils.isNotBlank(C3.getText()) && pattern.matcher(C3.getText()).matches() &&
                StringUtils.isNotBlank(C4.getText()) && pattern.matcher(C4.getText()).matches() &&
                StringUtils.isNotBlank(C5.getText()) && pattern.matcher(C5.getText()).matches() &&
                StringUtils.isNotBlank(C6.getText()) && pattern.matcher(C6.getText()).matches() &&
                StringUtils.isNotBlank(C7.getText()) && pattern.matcher(C7.getText()).matches() &&
                StringUtils.isNotBlank(C8.getText()) && pattern.matcher(C8.getText()).matches() &&
                StringUtils.isNotBlank(B1.getText()) && pattern.matcher(B1.getText()).matches() &&
                StringUtils.isNotBlank(B2.getText()) && pattern.matcher(B2.getText()).matches() &&
                StringUtils.isNotBlank(B3.getText()) && pattern.matcher(B3.getText()).matches() &&
                StringUtils.isNotBlank(B4.getText()) && pattern.matcher(B4.getText()).matches() &&
                StringUtils.isNotBlank(B5.getText()) && pattern.matcher(B5.getText()).matches() &&
                StringUtils.isNotBlank(B6.getText()) && pattern.matcher(B6.getText()).matches() &&
                StringUtils.isNotBlank(B7.getText()) && pattern.matcher(B7.getText()).matches() &&
                StringUtils.isNotBlank(B8.getText()) && pattern.matcher(B8.getText()).matches();
    }

    private List<ResultRow> calcScore(double[] c, double [] b) {
        List<ResultRow> resultRows = new ArrayList<ResultRow>(100);
        //главный цикл
        for (int p = 1; p < 100; p++) {
            ResultRow row =new ResultRow();
            row.w1 = p;
            row.w2 = 100 - p;
            row.BC = getBC(row.w1, row.w2, c, b);
            resultRows.add(row);
        }
        return resultRows;
    }

    private double getBC(int w1, int w2, double[] c, double[] b) {
        double ACmin = Double.MAX_VALUE;
        double[] AC = new double[8];
        double A;

        for (int i = 0; i < 8; i++) {
            A = ((w1 * c[i]) + (w2 * b[i])) / 100;
            AC[i] = A / Ae[i] * 100;
            if (AC[i] <= ACmin ) {
                ACmin = AC[i];
            }
        }

        return 100 - getKRAS(AC, ACmin);
    }

    private double getKRAS(double[] ac, double aCmin) {
        double SUM = 0;
        for (int i = 0; i < 8; i++) {
            SUM += (ac[i] - aCmin);
        }
        return SUM / 8;
    }

    private void printResult(List<ResultRow> result) {
        for (ResultRow row : result) {
            System.out.println("w1=" + row.w1 + "; w2=" + row.w2 + "; BC=" + row.BC);
        }
    }

    private void printInput() {
        System.out.println("C1=" + C1.getText() + "; B1=" + B1.getText());
        System.out.println("C2=" + C2.getText() + "; B2=" + B2.getText());
        System.out.println("C3=" + C3.getText() + "; B3=" + B3.getText());
        System.out.println("C4=" + C4.getText() + "; B4=" + B4.getText());
        System.out.println("C5=" + C5.getText() + "; B5=" + B5.getText());
        System.out.println("C6=" + C6.getText() + "; B6=" + B6.getText());
        System.out.println("C7=" + C7.getText() + "; B7=" + B7.getText());
        System.out.println("C8=" + C8.getText() + "; B8=" + B8.getText());
    }

    private String exportResultToXLSX(List<ResultRow> result, double[] c, double [] b) {
        Workbook book = new XSSFWorkbook();
        CellStyle doubleCellStyle = book.createCellStyle();
        doubleCellStyle.setDataFormat(book.createDataFormat().getFormat("#.#"));

        Sheet sheetResult = book.createSheet("Result");
        Row rowName = sheetResult.createRow(0);
        Cell w1Name = rowName.createCell(0);
        Cell w2Name = rowName.createCell(1);
        Cell bcName = rowName.createCell(2);
        w1Name.setCellValue("w1");
        w2Name.setCellValue("w2");
        bcName.setCellValue("BC");

        for (int i = 0, resultSize = result.size(); i < resultSize; i++) {
            ResultRow resultRow = result.get(i);

            Row row = sheetResult.createRow(i+1);

            Cell w1Cell = row.createCell(0);
            Cell w2Cell = row.createCell(1);
            Cell bcCell = row.createCell(2);

            w1Cell.setCellValue(resultRow.w1);
            w2Cell.setCellValue(resultRow.w2);
            bcCell.setCellValue(resultRow.BC);

            bcCell.setCellStyle(doubleCellStyle);
        }

        Sheet sheetInputData = book.createSheet("InputData");
        {
            Row name = sheetInputData.createRow(0);

            Cell C = name.createCell(0);
            Cell B = name.createCell(1);
            Cell AE = name.createCell(2);
            C.setCellValue("C");
            B.setCellValue("B");
            AE.setCellValue("Ae");

            for (int i = 0; i < 8; i++) {
                Row row = sheetInputData.createRow(i+1);
                Cell cellC = row.createCell(0);
                Cell cellB = row.createCell(1);
                Cell cellAe = row.createCell(2);
                cellC.setCellValue(c[i]);
                cellB.setCellValue(b[i]);
                cellAe.setCellValue(Ae[i]);
            }
        }


        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyHHmmss");
        String fileName =  "aminoasidscore_" + dateFormat.format(new Date()) + ".xlsx";

        try {
            book.write(new FileOutputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void renderLineChart(List<ResultRow> result, double[] c, double[] b, String name) {
        XYChart.Series<Number, Number> resultSeries = new XYChart.Series<Number, Number>();
        resultSeries.setName(name);
        ObservableList<XYChart.Data<Number, Number>> datas = FXCollections.observableArrayList();
        for (ResultRow resultRow : result) {
            datas.add(new XYChart.Data<Number, Number>(resultRow.w1, resultRow.BC));
        }
        resultSeries.setData(datas);

        lineChart.getData().add(0,resultSeries);
    }
}
