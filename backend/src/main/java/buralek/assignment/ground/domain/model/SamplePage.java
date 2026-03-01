package buralek.assignment.ground.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SamplePage {
    List<Sample> samples;
    boolean hasMore;
}