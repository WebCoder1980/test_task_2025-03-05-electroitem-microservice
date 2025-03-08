package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroItemType;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
import ru.isands.test.estore.dto.ElectroItemDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectroItemService {

    private final ElectroItemRepository electroItemRepository;

    public ElectroItemService(ElectroItemRepository electroItemRepository) {
        this.electroItemRepository = electroItemRepository;
    }

    public ElectroItemDTO add(ElectroItemDTO electroItemDTO) {
        ElectroItem electroitem = mapToEntity(electroItemDTO);
        ElectroItem savedElectroItem = electroItemRepository.save(electroitem);
        return mapToDTO(savedElectroItem);
    }

    public List<ElectroItemDTO> getAll(int start, int limit) {
        return electroItemRepository.findAll().stream()
                .sorted(Comparator.comparing(ElectroItem::getId))
                .skip(start)
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ElectroItemDTO getById(Long id) {
        return electroItemRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));
    }

    public ElectroItemDTO update(Long id, ElectroItemDTO electroItemDTO) {
        ElectroItem existingElectroItem = electroItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден"));

        updateEntityFromDTO(electroItemDTO, existingElectroItem);
        ElectroItem updatedElectroItem = electroItemRepository.save(existingElectroItem);
        return mapToDTO(updatedElectroItem);
    }

    public void delete(Long id) {
        if (!electroItemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Сотрудник не найден");
        }
        electroItemRepository.deleteById(id);
    }

    private ElectroItem mapToEntity(ElectroItemDTO electroItemDTO) {
        ElectroItem electroitem = new ElectroItem();
        updateEntityFromDTO(electroItemDTO, electroitem);
        return electroitem;
    }

    private void updateEntityFromDTO(ElectroItemDTO electroItemDTO, ElectroItem electroitem) {
        electroitem.setName(electroItemDTO.getName());

        ElectroItemType type = new ElectroItemType();
        type.setId(electroItemDTO.getType());
        electroitem.setType(type);

        electroitem.setPrice(electroItemDTO.getPrice());
        electroitem.setQuantity(electroItemDTO.getQuantity());
        electroitem.setArchive(electroItemDTO.getArchive());
        electroitem.setDescription(electroItemDTO.getDescription());
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<ElectroItemDTO> electroItems = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(";");

                ElectroItemDTO electroItemDTO = new ElectroItemDTO();
                electroItemDTO.setName(data[1].trim());
                electroItemDTO.setType(Long.parseLong(data[2].trim()));
                electroItemDTO.setPrice(Double.parseDouble(data[3].trim()));
                electroItemDTO.setQuantity(Integer.parseInt(data[4].trim()));
                electroItemDTO.setArchive("0".equals(data[5].trim()));
                electroItemDTO.setDescription(data[6].trim());

                electroItems.add(electroItemDTO);
            }

            saveAll(electroItems);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }

    public void saveAll(List<ElectroItemDTO> electroItemDTOs) {
        List<ElectroItem> electroItems = electroItemDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        electroItems.forEach(i -> electroItemRepository.save(i));
    }

    private ElectroItemDTO mapToDTO(ElectroItem electroitem) {
        ElectroItemDTO electroItemDTO = new ElectroItemDTO();
        electroItemDTO.setId(electroitem.getId());
        electroItemDTO.setName(electroitem.getName());
        electroItemDTO.setType(electroitem.getType().getId());
        electroItemDTO.setPrice(electroitem.getPrice());
        electroItemDTO.setQuantity(electroitem.getQuantity());
        electroItemDTO.setArchive(electroitem.getArchive());
        electroItemDTO.setDescription(electroitem.getDescription());
        return electroItemDTO;
    }
}
