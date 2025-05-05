package soft.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import soft.test.service.CalculateService;
import soft.test.service.ExcelFileUploadService;

import java.util.List;
import java.util.PriorityQueue;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Excel File Processing", description = "API для обработки Excel файлов")
public class XLSFileController {

    private final ExcelFileUploadService excelFileUploadService;
    private final CalculateService calculateService;

    public XLSFileController(ExcelFileUploadService excelFileUploadService,
                             CalculateService calculateService) {
        this.excelFileUploadService = excelFileUploadService;
        this.calculateService = calculateService;
    }

    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Найти N-ное минимальное число в файле",
            description = "Загружает XLSX файл с числами и возвращает N-ное минимальное значение",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная обработка файла",
                            content = @Content(
                                    schema = @Schema(implementation = Integer.class),
                                    examples = @ExampleObject(value = "5")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверный формат файла или параметров",
                            content = @Content(
                                    examples = @ExampleObject(value = "\"Неверный формат файла или параметров\"")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка обработки файла",
                            content = @Content(
                                    examples = @ExampleObject(value = "\"Ошибка обработки файла\"")
                            )
                    )
            }
    )
    public ResponseEntity<?> processFile(
            @Parameter(
                    description = "XLSX файл с числами",
                    required = true,
                    content = @Content(mediaType = "application/vnd.ms-excel")
            )
            @RequestPart("file") MultipartFile file,

            @Parameter(
                    description = "Сколько элементов пропустить",
                    required = true
            )
            @RequestParam("n") int n) {

        try {
            if (!excelFileUploadService.isValidFileType(file)) {
                return ResponseEntity.badRequest().body("Файл не прошел валидацию");
            }

            List<Integer> data = excelFileUploadService.readExcelFile(file);
            int result = calculateService.getResultByParameter(data, n);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Не смог обработать файл: " + e.getMessage());
        }
    }
}