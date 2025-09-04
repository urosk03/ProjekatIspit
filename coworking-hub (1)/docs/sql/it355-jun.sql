-- =====================================
-- Coworking Hub (4 tabele) - Final
-- =====================================

CREATE TABLE app_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  role INT NOT NULL,   -- 1 = MEMBER, 2 = MANAGER
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE room (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE,
  capacity INT NOT NULL,
  open_time TIME NOT NULL DEFAULT '08:00',
  close_time TIME NOT NULL DEFAULT '20:00',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_id BIGINT NOT NULL,
  member_id BIGINT NOT NULL,
  status INT NOT NULL DEFAULT 0,   -- 0=PENDING,1=APPROVED,2=REJECTED,3=CANCELLED
  starts_at DATETIME NOT NULL,
  ends_at DATETIME NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES room(id),
  CONSTRAINT fk_res_user FOREIGN KEY (member_id) REFERENCES app_user(id)
);

CREATE INDEX idx_user_email    ON app_user (email);
CREATE INDEX idx_room_name     ON room (name);
CREATE INDEX idx_res_room      ON reservation (room_id);
CREATE INDEX idx_res_member    ON reservation (member_id);
CREATE INDEX idx_res_status    ON reservation (status);
CREATE INDEX idx_res_starts_at ON reservation (starts_at);
