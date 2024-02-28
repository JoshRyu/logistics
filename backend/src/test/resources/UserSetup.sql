INSERT INTO member (id, username, password, role)
VALUES ((SELECT nextval('public.user_seq')), 'user1', '{bcrypt}$2a$10$O4vFwdhKm8tKngdtdJgl4OYZSWr4deUjju/5.drcQZRT9M8FIfR/a', 'USER');