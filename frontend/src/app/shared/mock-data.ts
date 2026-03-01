import { Location } from './models/location.model';
import { Sample, SamplePage } from './models/sample.model';
import { SampleStatistics } from './models/statistics.model';

// Thresholds match application.yaml: unitWeight > 25, waterContent > 100, shearStrength > 800

export const MOCK_LOCATIONS: Location[] = [
  { id: 'loc-1', name: 'North Site A', zoneId: 'Europe/Oslo', latitude: 60.51, longitude: 5.01 },
  { id: 'loc-2', name: 'East Site B',  zoneId: 'Europe/Oslo', latitude: 60.62, longitude: 5.23 },
  { id: 'loc-3', name: 'South Site C', zoneId: 'Europe/Oslo', latitude: 60.39, longitude: 4.89 },
];

export const MOCK_SAMPLES: Sample[] = [
  // North Site A — no violations
  { id: 's-124e5792-c81e-454f-a27c-e2ec89956f79', locationId: 'loc-1', timestampWithTimeZone: '2026-01-10T09:00:00+01:00', unitWeight: 18.5, waterContent: 45.2,  shearStrength: 120.0 },
  { id: 's-124e5792-c81e-454f-a27c-e2ec89956f79', locationId: 'loc-1', timestampWithTimeZone: '2026-01-15T11:30:00+01:00', unitWeight: 22.3, waterContent: 78.5,  shearStrength: 450.0 },
  // North Site A — unit weight violation
  { id: 's-0124e5792-c81e-454f-a27c-e2ec89956f7', locationId: 'loc-1', timestampWithTimeZone: '2026-01-20T14:00:00+01:00', unitWeight: 26.1, waterContent: 89.3,  shearStrength: 650.0 },

  // East Site B — water content violation
  { id: 's-04', locationId: 'loc-2', timestampWithTimeZone: '2026-02-05T08:15:00+01:00', unitWeight: 19.8, waterContent: 105.7, shearStrength: 320.0 },
  // East Site B — shear strength violation
  { id: 's-05', locationId: 'loc-2', timestampWithTimeZone: '2026-02-12T10:45:00+01:00', unitWeight: 21.5, waterContent: 92.4,  shearStrength: 830.0 },
  // East Site B — all three violations
  { id: 's-06', locationId: 'loc-2', timestampWithTimeZone: '2026-02-20T13:00:00+01:00', unitWeight: 25.8, waterContent: 112.0, shearStrength: 920.0 },

  // South Site C — no violations
  { id: 's-07', locationId: 'loc-3', timestampWithTimeZone: '2026-03-01T09:30:00+01:00', unitWeight: 17.2, waterContent: 55.8,  shearStrength: 280.0 },
  { id: 's-08', locationId: 'loc-3', timestampWithTimeZone: '2026-03-08T12:00:00+01:00', unitWeight: 20.6, waterContent: 73.1,  shearStrength: 540.0 },
];

export const MOCK_SAMPLE_PAGE: SamplePage = {
  data: MOCK_SAMPLES,
  hasMore: false,
  nextCursor: null,
};

// Statistics computed from all mock samples
// unitWeight exceeded: s-03 (26.1), s-06 (25.8)
// waterContent exceeded: s-04 (105.7), s-06 (112.0)
// shearStrength exceeded: s-05 (830.0), s-06 (920.0)
// avgWaterContent = (45.2+78.5+89.3+105.7+92.4+112.0+55.8+73.1) / 8 = 81.5
export const MOCK_STATISTICS: SampleStatistics = {
  filter: { locationId: null },
  averageWaterContent: 81.5,
  totalSamples: 8,
  samplesSurpassingThreshold: {
    unitWeight:    ['s-03', 's-06'],
    waterContent:  ['s-04', 's-06'],
    shearStrength: ['s-05', 's-06'],
  },
};
