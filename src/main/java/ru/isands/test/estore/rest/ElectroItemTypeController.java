package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.ElectroItemTypeDTO;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.service.ElectroItemTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "ElectroItemType", description = "Сервис для выполнения операций над типами товаров")
@RequestMapping("/estore/api/electroitemtype")
public class ElectroItemTypeController {

    private final ElectroItemTypeService electroItemTypeService;

    public ElectroItemTypeController(ElectroItemTypeService electroItemTypeService) {
        this.electroItemTypeService = electroItemTypeService;
    }

    @PostMapping
    @Operation(summary = "Добавить тип товара", responses = {
            @ApiResponse(responseCode = "200", description = "Тип товара добавлен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ElectroItemTypeDTO> add(@RequestBody ElectroItemTypeDTO electroItemTypeDTO) {
        return ResponseEntity.ok(electroItemTypeService.add(electroItemTypeDTO));
    }

    @GetMapping
    @Operation(summary = "Получить все типы товаров", parameters = {
            @Parameter(name = "start", description = "Номер первого в результате типа товара", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "limit", description = "Максимальное колличество типов товара в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
    }, responses = {
            @ApiResponse(responseCode = "200", description = "Список типов товара"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<List<ElectroItemTypeDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
        return ResponseEntity.ok(electroItemTypeService.getAll(start, limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип товара по ID", responses = {
            @ApiResponse(responseCode = "200", description = "Информация о типе товара"),
            @ApiResponse(responseCode = "404", description = "Тип товара не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
    })
    public ResponseEntity<ElectroItemTypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(electroItemTypeService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о типе товара", responses = {
            @ApiResponse(responseCode = "200", description = "Тип товара обновлен"),
            @ApiResponse(responseCode = "404", description = "Тип товара не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<ElectroItemTypeDTO> update(@PathVariable Long id, @RequestBody ElectroItemTypeDTO electroItemTypeDTO) {
        return ResponseEntity.ok(electroItemTypeService.update(id, electroItemTypeDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тип товара", responses = {
            @ApiResponse(responseCode = "204", description = "Тип товара удален"),
            @ApiResponse(responseCode = "404", description = "Тип товара не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        electroItemTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-csv")
    @Operation(summary = "Загрузить тип товара из CSV", responses = {
            @ApiResponse(responseCode = "200", description = "Тип товара успешно загружены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
        } else if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
        }

        try {
            electroItemTypeService.processCSVFile(file);
            return ResponseEntity.ok("Данные товар успешно загружены.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
        }
    }
}