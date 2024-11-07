CREATE TABLE deactivated_token(
    id uuid PRIMARY KEY,
    keep_until timestamp NOT NULL CHECK ( keep_until > now() )
);