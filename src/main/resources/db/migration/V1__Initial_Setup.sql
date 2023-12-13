CREATE SEQUENCE user_id_sequence;

CREATE TABLE "user"(
  id INT DEFAULT nextval('user_id_sequence') PRIMARY KEY,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  email TEXT NOT NULL,
  residence TEXT NOT NULL
);