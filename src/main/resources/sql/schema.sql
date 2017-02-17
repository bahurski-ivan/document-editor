CREATE SEQUENCE IF NOT EXISTS DOCUMENTS_PK_SEQ
START WITH 0
INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS DOCUMENT_TEMPLATES_PK_SEQ
START WITH 0
INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS FIELDS_INFO_PK_SEQ
START WITH 0
INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS USERS_PK_SEQ
START WITH 0
INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS _USERS (
  user_id      BIGINT DEFAULT USERS_PK_SEQ.nextval PRIMARY KEY,
  role         VARCHAR(255) NOT NULL,
  user_name    VARCHAR(50)  NOT NULL UNIQUE,
  password_md5 VARCHAR(32)  NOT NULL
);

CREATE TABLE IF NOT EXISTS DOCUMENT_TEMPLATES (
  template_id   BIGINT DEFAULT DOCUMENT_TEMPLATES_PK_SEQ.nextval PRIMARY KEY,
  template_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FIELDS_INFO (
  template_id    BIGINT       NOT NULL,
  field_id       BIGINT DEFAULT FIELDS_INFO_PK_SEQ.nextval PRIMARY KEY,
  technical_name VARCHAR(255) NOT NULL,
  display_name   VARCHAR(255) NOT NULL,
  type           VARCHAR(255) NOT NULL,
  ordinal        INT    DEFAULT 2147483647,

  FOREIGN KEY (template_id) REFERENCES DOCUMENT_TEMPLATES (template_id)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DOCUMENTS (
  template_id   BIGINT       NOT NULL,
  document_id   BIGINT DEFAULT DOCUMENTS_PK_SEQ.nextval PRIMARY KEY,
  document_name VARCHAR(255) NOT NULL,

  FOREIGN KEY (template_id) REFERENCES DOCUMENT_TEMPLATES (template_id)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FIELD_VALUES (
  document_id BIGINT NOT NULL,
  field_id    BIGINT NOT NULL,
  value       BLOB,

  PRIMARY KEY (document_id, field_id),

  FOREIGN KEY (document_id) REFERENCES DOCUMENTS (document_id)
  ON DELETE CASCADE,

  FOREIGN KEY (field_id) REFERENCES FIELDS_INFO (field_id)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FIELDS_ORDINALS (
  template_id BIGINT PRIMARY KEY,
  ordinals    BLOB DEFAULT NULL,

  FOREIGN KEY (template_id) REFERENCES DOCUMENT_TEMPLATES (template_id)
  ON DELETE CASCADE
);

CREATE TRIGGER FIELD_INFO_ADDED_TRIGGER AFTER INSERT
  ON FIELDS_INFO
FOR EACH ROW
CALL "ru.sbrf.docedit.dao.impl.h2.trigger.FieldInfoAddedTrigger";

CREATE TRIGGER FIELD_INFO_REMOVED_TRIGGER AFTER DELETE
  ON FIELDS_INFO
FOR EACH ROW
CALL "ru.sbrf.docedit.dao.impl.h2.trigger.FieldInfoRemovedTrigger";

CREATE TRIGGER FIELD_INFO_MODIFIED_TRIGGER AFTER UPDATE
  ON FIELDS_INFO
FOR EACH ROW
CALL "ru.sbrf.docedit.dao.impl.h2.trigger.FieldInfoModifiedTrigger";