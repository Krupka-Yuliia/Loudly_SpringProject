ALTER TABLE user
    ADD CONSTRAINT username UNIQUE (username);

ALTER TABLE user
    ADD CONSTRAINT email UNIQUE (email);
