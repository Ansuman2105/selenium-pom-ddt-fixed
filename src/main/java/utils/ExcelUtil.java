package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;
import java.util.*;

public class ExcelUtil {

    public static Object[][] getSheetData(String sheetName) {
        try (InputStream is = ExcelUtil.class.getClassLoader().getResourceAsStream("testdata.xlsx")) {

            if (is == null) {
                System.err.println("testdata.xlsx not found in resources!");
                return new Object[0][];
            }

            Workbook wb = WorkbookFactory.create(is);
            Sheet sh = wb.getSheet(sheetName);
            if (sh == null) {
                System.err.println("Sheet '" + sheetName + "' not found.");
                wb.close();
                return new Object[0][];
            }

            int rows = sh.getPhysicalNumberOfRows();
            if (rows < 2) { // no data rows
                wb.close();
                return new Object[0][];
            }

            int cols = sh.getRow(0).getLastCellNum();
            List<Object[]> data = new ArrayList<>();
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            for (int r = 1; r < rows; r++) { // skip header
                Row row = sh.getRow(r);
                if (row == null) continue;

                Object[] rowData = new Object[cols];
                for (int c = 0; c < cols; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    CellValue cellValue = evaluator.evaluate(cell);

                    rowData[c] = switch (cellValue.getCellType()) {
                        case STRING -> cellValue.getStringValue();
                        case NUMERIC -> String.valueOf(cellValue.getNumberValue());
                        case BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                        case BLANK -> "";
                        case ERROR -> "ERROR";
                        default -> "";
                    };
                }
                data.add(rowData);
            }

            wb.close();
            return data.toArray(new Object[0][]);

        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }
}
