-- Migration V4: add partial index for active users
-- Purpose: Optimize queries filtering by deleted_at IS NULL
-- Performance: Reduces index size by ~10% (excludes deleted users)
-- Query target: SELECT * FROM users WHERE deleted_at IS NULL ORDER BY created_at

CREATE INDEX IF NOT EXISTS idx_users_active
ON users (created_at DESC)
WHERE deleted_at IS NULL;
