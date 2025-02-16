DROP TABLE IF EXISTS AppUser;

CREATE TABLE AppUser
(
    AppUserID INT IDENTITY(1,1),
    Username NVARCHAR(50) NOT NULL,
    PasswordHash VARBINARY(64) NOT NULL,
    Salt NVARCHAR(50) NOT NULL,

    CONSTRAINT PK_AppUser_UserID PRIMARY KEY (AppUserID),
    CONSTRAINT UQ_AppUser_Username UNIQUE (Username),

    -- Password complexity requirements
    CONSTRAINT CK_AppUser_PasswordComplexity CHECK (
        LEN(PasswordHash) > 0 AND LEN(Salt) > 0
    )
);

-- Insert records with unique salts and salted hashes
INSERT INTO AppUser (Username, PasswordHash, Salt) 
VALUES
    ('mary.sue', HASHBYTES('SHA2_256', 'Cloudy#Morn22' + 's!P@1Y7x'), 's!P@1Y7x'),
    ('gary.stu', HASHBYTES('SHA2_256', 'Spring@202420242024' + 'k$L#3Wz5'), 'k$L#3Wz5'),
    ('phoebe.winters', HASHBYTES('SHA2_256', 'Cloudy#Morn22' + 'qF*2^X9z'), 'qF*2^X9z'),
    ('sam.rodgers', HASHBYTES('SHA2_256', 'Skyline!Run#77Run#66' + 'rT!6X@2p'), 'rT!6X@2p'),
    ('david.brown', HASHBYTES('SHA2_256', 'Ultra!Complex#Password123' + 'w@5p!X#1'), 'w@5p!X#1');
