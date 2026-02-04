CREATE TABLE guests (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  document VARCHAR(50) NOT NULL UNIQUE,
  phone VARCHAR(30) NOT NULL
);

CREATE TABLE check_ins (
  id BIGSERIAL PRIMARY KEY,
  guest_id BIGINT NOT NULL REFERENCES guests(id),
  entry_at TIMESTAMP NOT NULL,
  exit_at TIMESTAMP NOT NULL,
  has_vehicle BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_guests_name ON guests(name);
CREATE INDEX idx_guests_document ON guests(document);
CREATE INDEX idx_guests_phone ON guests(phone);

CREATE INDEX idx_checkins_guest_id ON check_ins(guest_id);
CREATE INDEX idx_checkins_entry_at ON check_ins(entry_at);
CREATE INDEX idx_checkins_exit_at ON check_ins(exit_at);