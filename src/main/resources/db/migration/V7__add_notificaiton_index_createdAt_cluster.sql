CREATE INDEX idx_notifications_created_at
    ON notifications (created_at);

CLUSTER notifications USING idx_notifications_created_at;