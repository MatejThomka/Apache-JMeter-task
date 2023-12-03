package com.mth.jMeterTask.entities.records;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TestPersonRecord(Integer id,String name, String lastname, String birthNumber) {
}
