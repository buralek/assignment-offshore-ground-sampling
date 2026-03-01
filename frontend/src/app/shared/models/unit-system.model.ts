export type UnitSystem = 'metric' | 'us';

export const UNIT_LABELS: Record<UnitSystem, { depth: string; unitWeight: string; waterContent: string; shearStrength: string }> = {
  metric: { depth: 'Depth (m)',  unitWeight: 'Unit Weight (kN/m³)', waterContent: 'Water Content (%)', shearStrength: 'Shear Strength (kPa)' },
  us:     { depth: 'Depth (ft)', unitWeight: 'Unit Weight (pcf)',    waterContent: 'Water Content (%)', shearStrength: 'Shear Strength (psf)' }
};