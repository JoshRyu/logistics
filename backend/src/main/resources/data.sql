INSERT INTO member (id, username, password, role)
VALUES (
        1,
        'admin',
        '{bcrypt}$2a$10$DN7bk6emH.YmUdWF6P08FeprVSDh/L1PnYrawj5XrQaTpCFZblQae',
        'ADMIN'
    ) ON CONFLICT (id) DO NOTHING;