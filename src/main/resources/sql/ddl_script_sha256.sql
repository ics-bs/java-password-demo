DROP TABLE IF EXISTS AppUser;

CREATE TABLE AppUser
(
    AppUserID       INTEGER IDENTITY(1,1),
    Username        NVARCHAR(50) NOT NULL,
    PasswordHash    VARBINARY(64) NOT NULL,

    CONSTRAINT PK_AppUser_UserID PRIMARY KEY (AppUserID),
    CONSTRAINT UQ_AppUser_Username UNIQUE (Username),

    -- Password complexity requirements
    CONSTRAINT CK_AppUser_PasswordComplexity CHECK (
        LEN(PasswordHash) > 0
    )
);

INSERT INTO AppUser (Username, PasswordHash) 
VALUES
    ('mary.sue', HASHBYTES('SHA2_256', 'Cloudy#Morn22')), 
    ('gary.stu', HASHBYTES('SHA2_256', 'Spring@202420242024')), 
    ('phoebe.winters', HASHBYTES('SHA2_256', 'Cloudy#Morn22')), 
    ('sam.rodgers', HASHBYTES('SHA2_256', 'Skyline!Run#77Run#66')), 
    ('david.brown', HASHBYTES('SHA2_256', 'Ultra!Complex#Password123'));
