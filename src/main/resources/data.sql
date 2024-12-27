use revature;

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (1, '1234', 'New York', 'United States', 'test@gmail.com', 'John', 'Doe', 'password', 'john_doe');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (2, '567', 'Los Angeles', 'United States', 'example@gmail.com', 'Jane', 'Doe', 'password', 'jane_doe');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (3, '910', 'Paris', 'France', 'france@gmail.com', 'Clark', 'Kent', 'password', 'clark_kent');

INSERT INTO user_account (id, address, city, country, email, first_name, last_name, password, username)
VALUES (4, '1112', 'London', 'United Kingdom', 'evil@gmail.com', 'Lex', 'Luthor', 'password', 'lex_luthor');

INSERT INTO user_profile (account_id, bio, display_name, is_private)
VALUES
	(1, 'bio', 'display_name', false),
	(2, 'bio', 'display name 2', false),
    (3, 'bio', 'display name 3', false),
    (4, 'bio', 'display name 4', true);

INSERT INTO travel_plan (id, account_id, is_favorited, is_published)
VALUES (1, 1, false, false);

INSERT INTO moderator_account (id, email, first_name, last_name, password, username)
VALUES (10, 'admin@gmail.com', 'Barry', 'Allen', 'password1', 'admin');

INSERT INTO post (id, created_at, travel_plan)
VALUES (1, '2019-01-01', 1);

INSERT INTO post_comment (id, commented_date, commented_on, content, made_by)
VALUES (1, '2019-01-01', 1, 'content', 3);

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
VALUES (1, 'Sydney', 'Australia', '2018-12-31', '2018-12-01', 1);

INSERT INTO travel_plan_collaborator(collaborator_id, travel_plan_id)
VALUES (3, 1);

INSERT INTO banned_user(account_id) VALUES (4);

INSERT INTO post_like(post_id, user_id) VALUES (1, 3);

INSERT INTO comment_like(comment_id, user_id) VALUES (1, 3);