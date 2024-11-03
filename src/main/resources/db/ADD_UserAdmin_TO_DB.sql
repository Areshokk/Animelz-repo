INSERT INTO users (id, username, password, email, active )
VALUES
    (99999, 'admin', '$2a$08$eApn9x3qPiwp6cBVRYaDXed3J/usFEkcZbuc3FDa74bKOpUzHR.S.', 'yrameda0404@gmail.com',true);

insert into user_role (user_id, roles) values (99999, 'ADMIN')