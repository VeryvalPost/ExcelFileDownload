package soft.test.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

@Service
public class ExcelFileUploadService {

    public static boolean isValidFileType(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return false;
            }
            String contentType = file.getContentType();
            return contentType != null &&
                    (contentType.equals("application/vnd.ms-excel") ||
                            contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) &&
                    Objects.requireNonNull(file.getOriginalFilename()).matches(".*\\.xlsx?");
        } catch (Exception e) {
            return false;
        }
    }

    public List<Integer> readExcelFile(MultipartFile file) {
        List<Integer> dataQueue = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    dataQueue.add((int) Math.round(cell.getNumericCellValue()));
                } else if (cell != null) {
                    throw new RuntimeException("Не смог добавить элемент, так как он не является числом");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка обработки Excel", e);
        }

        return dataQueue;
    }
}