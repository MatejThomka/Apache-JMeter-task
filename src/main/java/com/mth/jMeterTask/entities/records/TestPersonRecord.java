package com.mth.jMeterTask.entities.records;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record TestPersonRecord(String name, String lastname, String birthNumber) {
}
