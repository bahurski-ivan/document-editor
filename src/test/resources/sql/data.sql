INSERT INTO _USERS (role, user_name, password_md5)
VALUES ('REGULAR', 'user1', '');
INSERT INTO _USERS (role, user_name, password_md5)
VALUES ('ADMINISTRATOR', 'user2', '');

INSERT INTO DOCUMENT_TEMPLATES (template_name) VALUES ('шаблон 1');
INSERT INTO DOCUMENT_TEMPLATES (template_name) VALUES ('шаблон 2');

INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (0, 'field1', 'поле 1', 'INPUT');
INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (0, 'field2', 'поле 2', 'TEXTAREA');
INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (0, 'field2', 'поле 2', 'CHECKBOX');

INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (1, 'field1', 'поле 1', 'INPUT');
INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (1, 'field2', 'поле 2', 'INPUT');
INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type)
VALUES (1, 'field2', 'поле 2', 'INPUT');
