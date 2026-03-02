// Mirrors SampleSurpassingThresholdDto.java
// Each field is a list of sample IDs that exceed the threshold for that parameter
export interface SampleSurpassingThreshold {
  unitWeight: string[];
  waterContent: string[];
  shearStrength: string[];
}

// Mirrors SampleThresholdDto.java
export interface SampleThreshold {
  unitWeight: number;
  waterContent: number;
  shearStrength: number;
}

// Mirrors SampleStatisticResponse.java
export interface SampleStatistics {
  filter: { locationId: string | null };
  averageWaterContent: number;
  totalSamples: number;
  samplesSurpassingThreshold: SampleSurpassingThreshold;
  thresholds: SampleThreshold;
}