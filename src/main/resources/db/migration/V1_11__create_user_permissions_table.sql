CREATE TABLE IF NOT EXISTS user_permissions (
    user_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

-- Add index for better query performance
CREATE INDEX idx_user_permissions_user_id ON user_permissions (user_id);
CREATE INDEX idx_user_permissions_permission_id ON user_permissions (permission_id); 