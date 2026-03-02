
INSERT INTO location (id, name, zone_id, latitude, longitude) VALUES
    ('a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', 'North Sea Platform Alpha',    'Europe/Amsterdam', 57.1234,   2.4568),
    ('b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', 'North Sea Platform Beta',     'Europe/London',    57.8901,   1.2345),
    ('c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', 'Gulf of Mexico Platform A',   'America/Chicago',  28.9123,  -90.1234),
    ('d4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', 'Norwegian Continental Shelf', 'Europe/Oslo',      60.4720,   5.3241);

-- 5 samples for North Sea Platform Alpha (Europe/Amsterdam)
INSERT INTO sample (id, location_id, timestamp, zone_id, depth, unit_weight, water_content, shear_strength) VALUES
    ('00000000-0000-0000-0000-000000000001', 'a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', '2025-01-15 08:30:00', 'Europe/Amsterdam',  2.0, 18.5,  45.2,  320.0),
    ('00000000-0000-0000-0000-000000000002', 'a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', '2025-02-20 10:15:00', 'Europe/Amsterdam',  5.5, 22.1,  78.6,  550.0),
    ('00000000-0000-0000-0000-000000000003', 'a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', '2025-04-05 14:00:00', 'Europe/Amsterdam',  9.0, 26.3, 112.5,  420.0),
    ('00000000-0000-0000-0000-000000000004', 'a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', '2025-06-12 09:45:00', 'Europe/Amsterdam', 13.5, 19.8,  62.3,  855.5),
    ('00000000-0000-0000-0000-000000000005', 'a1a1a1a1-0001-0001-0001-a1a1a1a1a1a1', '2025-09-01 11:00:00', 'Europe/Amsterdam', 18.0, 24.7,  88.0,  710.0);

-- 5 samples for North Sea Platform Beta (Europe/London)
INSERT INTO sample (id, location_id, timestamp, zone_id, depth, unit_weight, water_content, shear_strength) VALUES
    ('00000000-0000-0000-0000-000000000006', 'b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', '2025-01-20 07:00:00', 'Europe/London',  1.5, 17.2,  35.4,  280.0),
    ('00000000-0000-0000-0000-000000000007', 'b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', '2025-03-10 13:30:00', 'Europe/London',  4.0, 27.8,  95.1,  925.0),
    ('00000000-0000-0000-0000-000000000008', 'b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', '2025-05-18 16:00:00', 'Europe/London',  8.0, 21.5, 107.3,  490.0),
    ('00000000-0000-0000-0000-000000000009', 'b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', '2025-07-22 08:15:00', 'Europe/London', 12.5, 20.0,  72.8,  380.0),
    ('00000000-0000-0000-0000-000000000010', 'b2b2b2b2-0002-0002-0002-b2b2b2b2b2b2', '2025-10-30 12:00:00', 'Europe/London', 17.0, 23.4,  83.5,  640.0);

-- 5 samples for Gulf of Mexico Platform A (America/Chicago)
INSERT INTO sample (id, location_id, timestamp, zone_id, depth, unit_weight, water_content, shear_strength) VALUES
    ('00000000-0000-0000-0000-000000000011', 'c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', '2025-02-08 15:00:00', 'America/Chicago',  3.0, 16.8,  42.0,  310.0),
    ('00000000-0000-0000-0000-000000000012', 'c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', '2025-04-14 09:00:00', 'America/Chicago',  6.5, 25.9,  98.7,  770.0),
    ('00000000-0000-0000-0000-000000000013', 'c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', '2025-06-25 11:30:00', 'America/Chicago', 10.0, 22.3, 116.2,  885.0),
    ('00000000-0000-0000-0000-000000000014', 'c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', '2025-08-17 14:45:00', 'America/Chicago', 14.5, 19.1,  55.6,  430.0),
    ('00000000-0000-0000-0000-000000000015', 'c3c3c3c3-0003-0003-0003-c3c3c3c3c3c3', '2025-11-03 10:00:00', 'America/Chicago', 20.0, 28.5,  91.3,  560.0);

-- 5 samples for Norwegian Continental Shelf (Europe/Oslo)
INSERT INTO sample (id, location_id, timestamp, zone_id, depth, unit_weight, water_content, shear_strength) VALUES
    ('00000000-0000-0000-0000-000000000016', 'd4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', '2025-01-10 06:30:00', 'Europe/Oslo',  2.5, 18.0,  38.9,  290.0),
    ('00000000-0000-0000-0000-000000000017', 'd4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', '2025-03-28 11:15:00', 'Europe/Oslo',  7.0, 21.7,  67.4,  510.0),
    ('00000000-0000-0000-0000-000000000018', 'd4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', '2025-07-04 13:00:00', 'Europe/Oslo', 11.5, 26.1, 104.8,  845.0),
    ('00000000-0000-0000-0000-000000000019', 'd4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', '2025-09-15 09:30:00', 'Europe/Oslo', 15.5, 20.5,  79.1,  620.0),
    ('00000000-0000-0000-0000-000000000020', 'd4d4d4d4-0004-0004-0004-d4d4d4d4d4d4', '2025-12-01 15:45:00', 'Europe/Oslo', 21.0, 23.9,  86.2,  730.0);