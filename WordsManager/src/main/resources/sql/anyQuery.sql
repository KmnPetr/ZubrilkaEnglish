
UPDATE person SET role='ROLE_ADMIN' WHERE id=1;

SELECT*FROM word
WHERE foreign_word IN (
    SELECT foreign_word
    FROM word
    GROUP BY foreign_word
    HAVING COUNT(*) > 1);