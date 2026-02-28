package buralek.assignment.ground.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Threshold {
    double unitWeight;
    double waterContent;
    double shearStrength;
}