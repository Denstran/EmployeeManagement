package com.manageemployee.employeemanagement.model.events.positionEvents;

import com.manageemployee.employeemanagement.model.Position;
import lombok.Data;

@Data
public class PositionDeleted {
    private final Position position;

    public PositionDeleted(Position position) {
        this.position = position;
    }
}
