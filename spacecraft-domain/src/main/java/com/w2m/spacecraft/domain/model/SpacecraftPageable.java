package com.w2m.spacecraft.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class SpacecraftPageable {

    private long totalItems;

    private List<Spacecraft> items;

    private long totalPages;

    private int currentPage;


}
