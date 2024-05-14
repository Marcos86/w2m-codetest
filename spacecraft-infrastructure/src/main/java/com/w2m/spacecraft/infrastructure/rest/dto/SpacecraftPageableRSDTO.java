package com.w2m.spacecraft.infrastructure.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class SpacecraftPageableRSDTO {

    private long totalItems;

    private List<SpacecraftRSDTO> items;

    private long totalPages;

    private int currentPage;


}
