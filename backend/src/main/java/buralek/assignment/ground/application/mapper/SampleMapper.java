package buralek.assignment.ground.application.mapper;

import buralek.assignment.ground.application.dto.SampleResponse;
import buralek.assignment.ground.domain.model.Sample;
import org.springframework.stereotype.Service;

@Service
public class SampleMapper {

    public SampleResponse toSampleResponse(Sample sample) {
        return SampleResponse.builder()
                .id(sample.getId())
                .locationId(sample.getLocation().getId())
                .timestampWithTimeZone(sample.getTimestamp().atZone(sample.getZoneId()).toOffsetDateTime())
                .depth(sample.getDepth())
                .unitWeight(sample.getUnitWeight())
                .waterContent(sample.getWaterContent())
                .shearStrength(sample.getShearStrength())
                .build();
    }
}
