CREATE TABLE AppUser
(
    UserID INT IDENTITY(1,1),
    Username NVARCHAR(50) NOT NULL,
    UserPassword NVARCHAR(MAX) NOT NULL,

    CONSTRAINT PK_AppUser_UserID PRIMARY KEY (UserID),
    CONSTRAINT UQ_AppUser_Username UNIQUE (Username),

    -- Password must meet the following complexity requirements:
    -- 1. Minimum length of 7 characters (LEN(UserPassword) > 6)
    -- 2. At least one uppercase letter (A-Z)
    -- 3. At least one lowercase letter (a-z)
    -- 4. At least one digit (0-9)
    -- 5. At least one special character (any non-alphanumeric)
    CONSTRAINT CK_AppUser_UserPassword CHECK (
        LEN(UserPassword) > 6
        AND PATINDEX('%[A-Z]%', UserPassword) > 0
        AND PATINDEX('%[a-z]%', UserPassword) > 0
        AND PATINDEX('%[0-9]%', UserPassword) > 0
        AND PATINDEX('%[^A-Za-z0-9]%', UserPassword) > 0
    )
);

INSERT INTO AppUser (Username, UserPassword) 
VALUES 
    ('mary.sue', 'Autumn#567'),    -- Shared password with Phoebe Winters
    ('gary.stu', 'Spring@2024'),   -- Unique password
    ('phoebe.winters', 'Autumn#567'), -- Shared password with Mary Sue
    ('sam.rodgers', 'Winter$890'), -- Unique password
    ('david.brown', 'Summer!123'); -- Unique password