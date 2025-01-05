use revature;

-- unhashed password is 'password'
INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (1, '1234', 'New York', 'United States', 'test@gmail.com', 'John', 'Doe',
    '$2a$10$seol2uAfLTyKI/gYKbL7G.XOAuOzZ2EAseMrgyI21Z9K9l9bhG.GO', 'john_doe');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (2, '567', 'Los Angeles', 'United States', 'example@gmail.com', 'Jane', 'Doe',
    '$2a$10$seol2uAfLTyKI/gYKbL7G.XOAuOzZ2EAseMrgyI21Z9K9l9bhG.GO', 'jane_doe');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (3, '910', 'Paris', 'France', 'france@gmail.com', 'Clark', 'Kent',
    '$2a$10$seol2uAfLTyKI/gYKbL7G.XOAuOzZ2EAseMrgyI21Z9K9l9bhG.GO', 'clark_kent');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (4, '1112', 'London', 'United Kingdom', 'evil@gmail.com', 'Lex', 'Luthor',
    '$2a$10$seol2uAfLTyKI/gYKbL7G.XOAuOzZ2EAseMrgyI21Z9K9l9bhG.GO', 'lex_luthor');

INSERT INTO user_profile (account_id, bio, display_name, is_private)
VALUES
	(1, 'bio', 'JoHnDoE', false),
	(2, 'bio', '', false),
    (3, 'bio', 'Superman', false),
    (4, 'bio', 'ilovekryptonite', true);

INSERT INTO travel_plan (id, account_id, is_favorited, is_published)
VALUES (1, 1, false, false),
    (2, 2, true, true), 
    (3, 3, true, true), 
    (4, 3, true, true), 
    (5, 1, false, true), 
    (6, 2, false, true);

INSERT INTO travel_plan_collaborator(collaborator_id, travel_plan_id)
VALUES (1, 1),
    (2, 2),
    (3, 3),
    (3, 4),
    (1, 5),
    (2, 6);

-- unhashed password is 'password1'
INSERT INTO moderator_account (id, email, first_name, last_name, password, username)
VALUES (10, 'admin@gmail.com', 'Barry', 'Allen',
    '$2a$10$E5DubIl00w/kunTkO4.qp.fjOc4RKjDUJS9h78Hj1bBKGemSgz7Zu', 'admin');

INSERT INTO post (id, created_at, travel_plan)
VALUES (1, '2019-01-01', 1),
    (2, '2019-01-01', 2),
    (3, '2019-01-01', 3),
    (4, '2019-01-01', 4),
    (5, '2019-01-01', 5),
    (6, '2019-01-01', 6);

INSERT INTO post_comment (id, commented_date, commented_on, content, made_by)
VALUES (1, '2019-01-01', 1, 'WOW! This trip looks amazing!', 3),
    (2, '2020-01-01', 3, "I can't believe you're going there at that time of year!!!", 2),
    (3, '2019-01-01', 2, "What places are you going to visit while you're there?", 3),
    (4, '2019-01-01', 3, "I went there with my mom last year and we had a great time!", 1),
    (5, '2019-01-01', 4, "The weather should be great!", 1),
    (6, '2019-01-01', 4, "Do you have room for one more?", 1),
    (7, '2019-01-01', 4, "Why do you always plan these great trips without me!", 1),
    (8, '2019-01-01', 6, "You better take a lot of great pictures while you're there.", 4);

INSERT INTO follow (follower, following)
VALUES
	(1, 2),
	(1, 3),
	(2, 1),
    (2, 3);

INSERT INTO follow_request (follower, following)
VALUES
	(1, 4),
	(2, 4),
    (3, 4);

INSERT INTO travel_plan_location(id, city, country, end_date, start_date, travel_plan_id)
VALUES (1, 'Sydney', 'Australia', '2018-12-31', '2018-12-01', 1),
    (2, 'New York', 'United States', '2019-12-31', '2019-12-24', 2),
    (3, 'Paris', 'France', '2020-1-05', '2020-1-15', 3),
    (4, 'Beijing', 'China', '2021-2-10', '2021-2-28', 4),
    (5, 'Rio de Janeiro', 'Brazil', '2022-5-18', '2022-5-25', 5),
    (6, 'Abu Dhabi', 'United Arab Emirates', '2023-8-12', '2023-9-12', 6);

INSERT INTO banned_user(account_id) VALUES (4);

INSERT INTO post_like(post_id, user_id) VALUES (1, 3), (2, 3), (3, 2), (4, 3), (5, 3), (6, 3), (3, 4), (4, 4);

INSERT INTO comment_like(comment_id, user_id) VALUES (1, 3);