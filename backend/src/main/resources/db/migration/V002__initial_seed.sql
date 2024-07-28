INSERT INTO metrics (name, standard_unit) VALUES
('Distance', 'm'),
('Time', 's'),
('Sets', 'sets'),
('Reps', 'reps'),
('Weight', 'kg');

INSERT INTO units (unit, name, conversion_factor, metric_id) VALUES
('km', 'kilometer', 1000, (SELECT id FROM metrics WHERE name='Distance')),
('m', 'meter', 1, (SELECT id FROM metrics WHERE name='Distance')),
('sec', 'second', 1, (SELECT id FROM metrics WHERE name='Time')),
('min', 'minute', 60, (SELECT id FROM metrics WHERE name='Time')),
('h', 'hour', 3600, (SELECT id FROM metrics WHERE name='Time')),
('set', 'set', 1, (SELECT id FROM metrics WHERE name='Sets')),
('rep', 'rep', 1, (SELECT id FROM metrics WHERE name='Reps')),
('kg', 'kilogram', 1, (SELECT id FROM metrics WHERE name='Weight')),
('g', 'gram', 1000, (SELECT id FROM metrics WHERE name='Weight'));

INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_DEMO'),
('ROLE_ADMIN');