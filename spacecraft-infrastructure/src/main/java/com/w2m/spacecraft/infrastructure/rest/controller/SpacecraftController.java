package com.w2m.spacecraft.infrastructure.rest.controller;

import com.w2m.spacecraft.domain.model.SpacecraftPageable;
import com.w2m.spacecraft.domain.usecase.SpacecraftUseCase;
import com.w2m.spacecraft.infrastructure.aspect.NegativeId;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftPageableRSDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRQDTO;
import com.w2m.spacecraft.infrastructure.rest.dto.SpacecraftRSDTO;
import com.w2m.spacecraft.infrastructure.rest.mapper.SpacecraftRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("spacecrafts")
public class SpacecraftController {

    @SuppressWarnings("squid:S1075") // Constant, not applies Sonar suggestion
    private static final String PATH_ID = "/{id}";

    private static final String QUERY_PARAM_NAME = "name";

    private static final String QUERY_PARAM_PAGE = "page";

    private static final String QUERY_PARAM_SIZE = "size";

    private static final String DEFAULT_PAGE = "0";

    private static final String DEFAULT_SIZE = "3";

    private final SpacecraftRestMapper restMapper;

    private final SpacecraftUseCase spacecraftUseCase;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SpacecraftRSDTO> createSpacecraft(@RequestBody final SpacecraftRQDTO spacecraftRQDTO) {
        var spacecraftIn = restMapper.toSpacecraft(spacecraftRQDTO);
        var spacecraftResult = spacecraftUseCase.create(spacecraftIn);
        return ResponseEntity.ok(restMapper.toSpacecraftRSDTO(spacecraftResult));
    }

    @GetMapping(PATH_ID)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpacecraftRSDTO> findSpacecraftById(@PathVariable final long id) {
        var spacecraft = spacecraftUseCase.findById(id);
        var spacecraftRSDTO = restMapper.toSpacecraftRSDTO(spacecraft);
        return ResponseEntity.ok(spacecraftRSDTO);
    }

    @PatchMapping(PATH_ID)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpacecraftRSDTO> updateSpacecraft(@PathVariable final long id,
             @RequestBody final SpacecraftRQDTO spacecraftRQDTO) {
        var spacecraftIn = restMapper.toSpacecraft(spacecraftRQDTO);
        spacecraftIn.setId(id);
        var spacecraftUpdated = spacecraftUseCase.update(spacecraftIn);
        var spacecraftRSDTO = restMapper.toSpacecraftRSDTO(spacecraftUpdated);
        return ResponseEntity.ok(spacecraftRSDTO);
    }


    @DeleteMapping(PATH_ID)
    @ResponseStatus(HttpStatus.OK)
    @NegativeId
    public void deleteSpacecraft (@PathVariable final long id) {
        spacecraftUseCase.delete(id);
    }


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpacecraftPageableRSDTO> findSpacecrafts(
            @RequestParam (name = QUERY_PARAM_NAME, required = false) final String name,
            @RequestParam (name = QUERY_PARAM_PAGE, required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam (name = QUERY_PARAM_SIZE, required = false, defaultValue = DEFAULT_SIZE) int size) {
        SpacecraftPageable spacecraftPageable = null;
        if (null != name) {
            spacecraftPageable = spacecraftUseCase.findAllByName(name, page, size);
        } else {
            spacecraftPageable = spacecraftUseCase.findAll(page, size);
        }
        var spacecraftPageableRSDTO = restMapper.toSpacecraftPageableRSDTO(spacecraftPageable);
        return ResponseEntity.ok(spacecraftPageableRSDTO);
    }
}
