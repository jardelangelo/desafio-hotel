ALTER TABLE check_ins
ADD COLUMN total_amount NUMERIC(14,2);

CREATE INDEX idx_checkins_guest_exit_at ON check_ins(guest_id, exit_at);