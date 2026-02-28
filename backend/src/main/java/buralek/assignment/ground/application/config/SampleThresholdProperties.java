package buralek.assignment.ground.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application.sample-thresholds")
@Getter
@Setter
public class SampleThresholdProperties {
    private double unitWeight;
    private double waterContent;
    private double shearStrength;
}