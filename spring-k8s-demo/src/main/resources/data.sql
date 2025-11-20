-- Initial data (will be executed after schema creation)
-- Note: This will only run if spring.jpa.hibernate.ddl-auto is set to create or create-drop
-- For production, use update mode and manage data through API

INSERT IGNORE INTO users (name, email, phone, created_at, updated_at) VALUES
('John Doe', 'john.doe@example.com', '+1234567890', NOW(), NOW()),
('Jane Smith', 'jane.smith@example.com', '+0987654321', NOW(), NOW()),
('Bob Johnson', 'bob.johnson@example.com', '+1122334455', NOW(), NOW());

