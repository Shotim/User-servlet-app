package com.leverx.user.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Positive;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class PointsTransferDto {

    Integer recipientId;

    @Positive
    Integer animalPoints;
}
