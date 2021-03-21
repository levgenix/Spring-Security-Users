INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_GUEST');
INSERT INTO spring_security_users.users (id, email, enabled, name, last_name, password) VALUES (1, 'admin', true, 'Василий', 'Уткин', '$2y$10$kbBc2/YyhalAHuK.SRiFPeu/iENCtVjUS9sK4A3/4b5EXdgqcj0cy');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
