package com.w2m.spacecraft.infrastructure.rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SpacecraftRSDTO extends SpacecraftRDTO {

    private Long id;

}
