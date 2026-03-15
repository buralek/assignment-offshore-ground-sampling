export type UnitSystem = 'metric' | 'us';

export const UNIT_LABELS: Record<UnitSystem, { depth: string; unitWeight: string; waterContent: string; shearStrength: string }> = {
  metric: { depth: 'Depth (m)',  unitWeight: 'Unit Weight (kN/m³)', waterContent: 'Water Content (%)', shearStrength: 'Shear Strength (kPa)' },
  us:     { depth: 'Depth (ft)', unitWeight: 'Unit Weight (pcf)',    waterContent: 'Water Content (%)', shearStrength: 'Shear Strength (psf)' }
};

// Multipliers to apply when converting FROM metric TO the given system
export const UNIT_CONVERSIONS: Record<UnitSystem, { unitWeight: number; shearStrength: number, depth: number }> = {
  metric: { unitWeight: 1,       shearStrength: 1 , depth: 1},
  us:     { unitWeight: 6.36587, shearStrength: 20.8854, depth: 3.28 },
};

// Short unit symbols for display (e.g. threshold labels)
export const UNIT_SYMBOLS: Record<UnitSystem, { unitWeight: string; waterContent: string; shearStrength: string }> = {
  metric: { unitWeight: 'kN/m³', waterContent: '%', shearStrength: 'kPa' },
  us:     { unitWeight: 'pcf',   waterContent: '%', shearStrength: 'psf' },
};
