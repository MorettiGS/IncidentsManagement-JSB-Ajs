INSERT INTO app_user (id, email, password, name, created_at, active) VALUES
('11111111-1111-1111-1111-111111111111', 'sarah.connor@email.com', '$2a$10$rL.8Xp/6jYd.6U1sWZzXX.FO6k7Jd6V6q6k6Z6Z6Z6Z6Z6Z6Z6Z6Z', 'Sarah Connor', NOW(), true),
('22222222-2222-2222-2222-222222222222', 'john.doe@email.com', '$2a$10$rL.8Xp/6jYd.6U1sWZzXX.FO6k7Jd6V6q6k6Z6Z6Z6Z6Z6Z6Z6Z6Z', 'John Doe', NOW(), true),
('33333333-3333-3333-3333-333333333333', 'alice.smith@email.com', '$2a$10$rL.8Xp/6jYd.6U1sWZzXX.FO6k7Jd6V6q6k6Z6Z6Z6Z6Z6Z6Z6Z6Z', 'Alice Smith', NOW(), true),
('44444444-4444-4444-4444-444444444444', 'bob.johnson@email.com', '$2a$10$rL.8Xp/6jYd.6U1sWZzXX.FO6k7Jd6V6q6k6Z6Z6Z6Z6Z6Z6Z6Z6Z', 'Bob Johnson', NOW(), true);

INSERT INTO user_roles (user_id, roles) VALUES
('11111111-1111-1111-1111-111111111111', 'ROLE_ADMIN'),
('22222222-2222-2222-2222-222222222222', 'ROLE_READ'),
('22222222-2222-2222-2222-222222222222', 'ROLE_WRITE'),
('33333333-3333-3333-3333-333333333333', 'ROLE_READ'),
('44444444-4444-4444-4444-444444444444', 'ROLE_WRITE');

INSERT INTO incident (id, titulo, descricao, prioridade, status, responsavel_email, tags, data_abertura, data_atualizacao) VALUES
('55555555-5555-5555-5555-555555555555', 'Database Connection Timeout', 'Users experiencing intermittent database connection timeouts during peak hours. Needs investigation into connection pool configuration.', 'ALTA', 'EM_ANDAMENTO', 'sarah.connor@email.com', '["database", "performance", "timeout"]', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 hour'),
('66666666-6666-6666-6666-666666666666', 'UI Button Alignment Issue', 'Submit button misaligned on mobile view of customer registration form. Affects iOS Safari browsers only.', 'BAIXA', 'RESOLVIDA', 'john.doe@email.com', '["ui", "frontend", "mobile"]', NOW() - INTERVAL '5 days', NOW() - INTERVAL '2 days'),
('77777777-7777-7777-7777-777777777777', 'Payment Gateway Integration Failing', 'Third-party payment gateway API returning 500 errors for 15% of transactions. Urgent fix required.', 'ALTA', 'ABERTA', 'sarah.connor@email.com', '["payment", "api", "integration"]', NOW() - INTERVAL '1 day', NOW() - INTERVAL '3 hours'),
('88888888-8888-8888-8888-888888888888', 'Email Notification Delay', 'System notifications are being delayed by approximately 30 minutes. Suspected queue processing issue.', 'MEDIA', 'EM_ANDAMENTO', 'alice.smith@email.com', '["email", "notifications", "queue"]', NOW() - INTERVAL '3 days', NOW() - INTERVAL '4 hours'),
('99999999-9999-9999-9999-999999999999', 'Report Generation Performance', 'Monthly report generation taking over 2 hours to complete. Needs optimization for larger datasets.', 'MEDIA', 'ABERTA', 'bob.johnson@email.com', '["reports", "performance", "optimization"]', NOW() - INTERVAL '1 day', NOW() - INTERVAL '6 hours');

INSERT INTO comment (id, incident_id, autor, mensagem, data_criacao) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '55555555-5555-5555-5555-555555555555', 'Sarah Connor', 'Increased database connection pool size from 50 to 100. Monitoring for improvements.', NOW() - INTERVAL '1 day'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '55555555-5555-5555-5555-555555555555', 'John Doe', 'Also added connection validation query to prevent stale connections.', NOW() - INTERVAL '23 hours'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', '66666666-6666-6666-6666-666666666666', 'Alice Smith', 'Fixed CSS alignment issue for mobile Safari. Tested on multiple devices.', NOW() - INTERVAL '2 days'),
('dddddddd-dddd-dddd-dddd-dddddddddddd', '77777777-7777-7777-7777-777777777777', 'Sarah Connor', 'Contacted payment gateway support. They report increased error rates on their end.', NOW() - INTERVAL '5 hours'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '88888888-8888-8888-8888-888888888888', 'Bob Johnson', 'Identified stuck message in RabbitMQ queue. Restarted consumer service.', NOW() - INTERVAL '3 hours');
