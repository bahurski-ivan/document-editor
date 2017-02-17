INSERT INTO _USERS (role, user_name, password_md5)
VALUES ('REGULAR', 'user1', '');
INSERT INTO _USERS (role, user_name, password_md5)
VALUES ('ADMINISTRATOR', 'user2', '');

INSERT INTO DOCUMENT_TEMPLATES (template_name) VALUES ('шаблон 1');
INSERT INTO DOCUMENT_TEMPLATES (template_name) VALUES ('шаблон 2');

INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (0, 0, 'field1', 'поле 1');
INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (0, 1, 'field2', 'поле 2');
INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (0, 2, 'field2', 'поле 2');

INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (1, 0, 'field1', 'поле 1');
INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (1, 1, 'field2', 'поле 2');
INSERT INTO FIELDS_INFO (template_id, display_ordinal, technical_name, display_name)
VALUES (1, 2, 'field2', 'поле 2');