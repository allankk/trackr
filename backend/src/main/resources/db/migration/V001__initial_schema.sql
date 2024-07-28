CREATE TABLE metrics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    standard_unit VARCHAR(255) NOT NULL
);

CREATE TABLE units (
    id BIGSERIAL PRIMARY KEY,
    unit VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    conversion_factor DOUBLE PRECISION NOT NULL,
    metric_id BIGINT,
    FOREIGN KEY (metric_id) REFERENCES metrics(id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE activity_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_default BOOLEAN NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE activity_type_metrics (
    activity_type_id BIGINT,
    metric_id BIGINT,
    PRIMARY KEY (activity_type_id, metric_id),
    FOREIGN KEY (activity_type_id) REFERENCES activity_type(id),
    FOREIGN KEY (metric_id) REFERENCES metrics(id)
);

CREATE TABLE activity_group (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(255),
    description VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE activity_group_activity_types (
    activity_group_id BIGINT,
    activity_type_id BIGINT,
    PRIMARY KEY (activity_group_id, activity_type_id),
    FOREIGN KEY (activity_group_id) REFERENCES activity_group(id),
    FOREIGN KEY (activity_type_id) REFERENCES activity_type(id)
);

CREATE TABLE activity_session (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    notes VARCHAR(255),
    date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE activity_session_activity_types (
    activity_session_id BIGINT,
    activity_type_id BIGINT,
    PRIMARY KEY (activity_session_id, activity_type_id),
    FOREIGN KEY (activity_session_id) REFERENCES activity_session(id),
    FOREIGN KEY (activity_type_id) REFERENCES activity_type(id)
);

CREATE TABLE activity_session_metric (
    id BIGSERIAL PRIMARY KEY,
    activity_session_id BIGINT,
    activity_type_id BIGINT,
    metric_id BIGINT,
    unit_id BIGINT,
    value DOUBLE PRECISION,
    FOREIGN KEY (activity_session_id) REFERENCES activity_session(id),
    FOREIGN KEY (activity_type_id) REFERENCES activity_type(id),
    FOREIGN KEY (metric_id) REFERENCES metrics(id),
    FOREIGN KEY (unit_id) REFERENCES units(id)
);
