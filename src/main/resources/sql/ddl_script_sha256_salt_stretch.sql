DROP TABLE IF EXISTS AppUser;

CREATE TABLE AppUser (
    AppUserID       INTEGER IDENTITY(1,1),
    Username        NVARCHAR(50)    NOT NULL,
    PasswordHash    VARBINARY(64)   NOT NULL,
    Salt            NVARCHAR(128)   NOT NULL,
    Iterations      INTEGER         NOT NULL,

    CONSTRAINT PK_AppUser_UserID PRIMARY KEY (AppUserID),
    CONSTRAINT UQ_AppUser_Username UNIQUE (Username),

    -- Password complexity requirements
    CONSTRAINT CK_AppUser_PasswordComplexity CHECK (
        DATALENGTH(PasswordHash) > 0 
        AND LEN(Salt) > 0 
        AND Iterations > 0
    )
);

-- Insert records with unique salts and PBKDF2-stretched binary hashes
-- Hashes were precomputed using PBKDF2WithHmacSHA256 (100,000 iterations)
INSERT INTO AppUser (Username, PasswordHash, Salt, Iterations) 
VALUES
    ('mary.sue', 0xD0F30419820495FBBCA2B56C74B8A534E7167C868684114B9DA8DD2E53CF4F5E, 's!P@1Y7x', 100000),
    ('gary.stu', 0x8EC41D8CAB306FBB55126343FE8B4EA354CAFEEE526D7C56A1B6C190C0F4E966, 'k$L#3Wz5', 100000),
    ('phoebe.winters', 0xAACD00B8DD0EF3CFA125F89E675A6B5829FE8F106343D2A402528C76D6881BB0, 'qF*2^X9z', 100000),
    ('sam.rodgers', 0xE78946A1AAFB0014AACA77786E3F62A7B081D6ACAA1D18DEB2070FBBAAB442D1, 'rT!6X@2p', 100000),
    ('david.brown', 0xC25573B89FAD4DDDA8BC09F40747EE6F04F610D511045928846583F84661DF93, 'w@5p!X#1', 100000);

