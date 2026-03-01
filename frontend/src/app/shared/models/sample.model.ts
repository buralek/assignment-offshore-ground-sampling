// Mirrors SampleResponse.java
export interface Sample {
  id: string;
  locationId: string;
  timestampWithTimeZone: string; // OffsetDateTime — ISO-8601 string e.g. "2026-07-01T10:15:30+02:00"
  unitWeight: number;            // kN/m³
  waterContent: number;          // %
  shearStrength: number;         // kPa
}

// Mirrors SampleRequest.java
export interface SampleRequest {
  locationId: string;
  samplingTimestamp: string;     // Instant — UTC ISO-8601 string e.g. "2026-07-01T08:15:30Z"
  unitWeight: number;
  waterContent: number;
  shearStrength: number;
}

// Mirrors SampleCursor.java
export interface SampleCursor {
  afterTimestamp: string;
  afterId: string;
}

// Mirrors SamplePageResponse.java
export interface SamplePage {
  data: Sample[];
  hasMore: boolean;
  nextCursor: SampleCursor | null;
}