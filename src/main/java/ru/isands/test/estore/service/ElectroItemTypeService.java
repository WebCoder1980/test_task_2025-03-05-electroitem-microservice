package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroItemType;
import ru.isands.test.estore.dao.repo.ElectroItemTypeRepository;
import ru.isands.test.estore.dto.ElectroItemDTO;
import ru.isands.test.estore.dto.ElectroItemTypeDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectroItemTypeService {

    private final ElectroItemTypeRepository electroItemTypeRepository;

    public ElectroItemTypeService(ElectroItemTypeRepository electroItemTypeRepository) {
        this.electroItemTypeRepository = electroItemTypeRepository;
    }

    public ElectroItemTypeDTO add(ElectroItemTypeDTO electroItemTypeDTO) {
        ElectroItemType electroItemType = mapToEntity(electroItemTypeDTO);
        ElectroItemType savedElectroItemType = electroItemTypeRepository.save(electroItemType);
        return mapToDTO(savedElectroItemType);
    }

    public List<ElectroItemTypeDTO> getAll(int start, int limit) {
        return electroItemTypeRepository.findAll().stream()
                .map(this::mapToDTO)
                .sorted(Comparator.comparing(ElectroItemTypeDTO::getId))
                .skip(start)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public ElectroItemTypeDTO getById(Long id) {
        return electroItemTypeRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Тип товара не найден"));
    }

    public ElectroItemTypeDTO update(Long id, ElectroItemTypeDTO electroItemTypeDTO) {
        ElectroItemType existingElectroItemType = electroItemTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Тип товара не найден"));

        existingElectroItemType.setName(electroItemTypeDTO.getName());
        ElectroItemType updatedElectroItemType = electroItemTypeRepository.save(existingElectroItemType);
        return mapToDTO(updatedElectroItemType);
    }

    public void delete(Long id) {
        if (!electroItemTypeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Тип товара не найден");
        }
        electroItemTypeRepository.deleteById(id);
    }

    private ElectroItemType mapToEntity(ElectroItemTypeDTO electroItemTypeDTO) {
        ElectroItemType electroItemType = new ElectroItemType();
        electroItemType.setName(electroItemTypeDTO.getName());
        return electroItemType;
    }

    private ElectroItemTypeDTO mapToDTO(ElectroItemType electroItemType) {
        ElectroItemTypeDTO electroItemTypeDTO = new ElectroItemTypeDTO();
        electroItemTypeDTO.setId(electroItemType.getId());
        electroItemTypeDTO.setName(electroItemType.getName());
        return electroItemTypeDTO;
    }

    public void saveAll(List<ElectroItemTypeDTO> electroItemTypeDTOs) {
        List<ElectroItemType> electroItemTypes = electroItemTypeDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        electroItemTypeRepository.saveAll(electroItemTypes);
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<ElectroItemTypeDTO> electroItemTypes = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(";");

                ElectroItemTypeDTO electroItemTypeDTO = new ElectroItemTypeDTO();
                electroItemTypeDTO.setName(data[1].trim());

                electroItemTypes.add(electroItemTypeDTO);
            }

            saveAll(electroItemTypes);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }
}
