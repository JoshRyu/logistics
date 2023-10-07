INSERT INTO member (id, username, password, role)
VALUES (
        1,
        'admin',
        '{bcrypt}$2a$05$pgzjl3PrenmR85feuZctf.VDL3qUUq/aQl4ELHbJAa7FY.pNODW3y',
        'ADMIN'
    ) ON CONFLICT (id) DO NOTHING;