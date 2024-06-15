INSERT INTO security.user_tokens(id, user_id, expiration_at, token, token_type)
VALUES ('511a084f-6065-44cd-b7b7-a21acf2eb77a', 'b3afa636-8006-42fe-961e-21ae926b3265', NOW() - INTERVAL '1 day',
        'd16089a6-f8b5-41fc-97b7-8f9d42ecf55f', 'ACTIVATION'),
       ('e6bf32a9-22cc-44ec-85eb-4f7632dabfd1', 'b3afa636-8006-42fe-961e-21ae926b3265', NOW() + INTERVAL '3 day',
        'e6bf32a9-22cc-44ec-85eb-4f7632dabfd1', 'ACTIVATION'),
       ('9f8a2458-2fb6-4d18-b57b-ee33f9ec0336', 'b3afa636-8006-42fe-961e-21ae926b3265', NOW() + INTERVAL '3 day',
        'bd935732-57b2-4302-bb92-0704b6d78648', 'ACTIVATION');;