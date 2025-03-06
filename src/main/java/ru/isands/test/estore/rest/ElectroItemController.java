package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.dto.ErrorDTO;
import ru.isands.test.estore.service.ElectroItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@Tag(name = "ElectroItem", description = "Сервис для выполнения операций над товаром")
@RequestMapping("/estore/api/electroitem")
public class ElectroItemController {

	private final ElectroItemService electroItemService;

	public ElectroItemController(ElectroItemService electroItemService) {
		this.electroItemService = electroItemService;
	}

	@PostMapping
	@Operation(summary = "Добавить товар", responses = {
			@ApiResponse(responseCode = "200", description = "Товар добавлен"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<ElectroItemDTO> add(@RequestBody ElectroItemDTO electroItemDTO) {
		return ResponseEntity.ok(electroItemService.add(electroItemDTO));
	}

	@GetMapping
	@Operation(summary = "Получить все товары", parameters = {
			@Parameter(name = "start", description = "Номер первого в результате товара", schema = @Schema(type = "integer", defaultValue = "0")),
			@Parameter(name = "limit", description = "Максимальное колличество товаров в результате", schema = @Schema(type = "integer", defaultValue = "1000000"))
			}, responses = {
			@ApiResponse(responseCode = "200", description = "Список товаров"),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<List<ElectroItemDTO>> getAll(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "limit", defaultValue = "1000000") int limit) {
		return ResponseEntity.ok(electroItemService.getAll(start, limit));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Получить товар по ID", responses = {
			@ApiResponse(responseCode = "200", description = "Информация о товаре"),
			@ApiResponse(responseCode = "404", description = "Товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
	})
	public ResponseEntity<ElectroItemDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(electroItemService.getById(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Обновить информацию о товар", responses = {
			@ApiResponse(responseCode = "200", description = "Товар обновлен"),
			@ApiResponse(responseCode = "404", description = "Товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<ElectroItemDTO> update(@PathVariable Long id, @RequestBody ElectroItemDTO electroItemDTO) {
		return ResponseEntity.ok(electroItemService.update(id, electroItemDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Удалить товар", responses = {
			@ApiResponse(responseCode = "204", description = "Товар удален"),
			@ApiResponse(responseCode = "404", description = "Товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		electroItemService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/upload-csv")
	@Operation(summary = "Загрузить товар из CSV", responses = {
			@ApiResponse(responseCode = "200", description = "Товар успешно загружены"),
			@ApiResponse(responseCode = "404", description = "Товар не найден", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
			@ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
	})
	public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
        } else if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пожалуйста, загрузите корректный CSV файл.");
        }

		try {
			electroItemService.processCSVFile(file);
			return ResponseEntity.ok("Данные товар успешно загружены.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла.");
		}
	}
}
