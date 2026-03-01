package buralek.assignment.ground.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SamplePageResponse {
    List<SampleResponse> data;
    boolean hasMore;
    SampleCursor nextCursor;
}