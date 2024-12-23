use revature;

INSERT into user_account(id, address, city, country, email, first_name, last_name, password, password_salt, username)
VALUES (1, '1234', 'New York', 'United States', 'test@gmail.com', 'John', 'Doe', 'password', 'password_salt', 'john_doe');

INSERT into user_account(id, address, city, country, email, first_name, last_name, password, password_salt, username)
VALUES (2, '567', 'Los Angeles', 'United States', 'example@gmail.com', 'Jane', 'Doe', 'password', 'password_salt', 'jane_doe');

INSERT into user_account(id, address, city, country, email, first_name, last_name, password, password_salt, username)
VALUES (3, '910', 'Paris', 'France', 'france@gmail.com', 'Clark', 'Kent', 'password', 'password_salt', 'clark_kent');

INSERT into user_account(id, address, city, country, email, first_name, last_name, password, password_salt, username)
VALUES (4, '1112', 'London', 'United Kingdom', 'evil@gmail.com', 'Lex', 'Luthor', 'password', 'password_salt', 'lex_luthor');   

INSERT into user_profile(account_id, bio, display_name, is_private)
VALUES (1, 'bio', 'display_name', false);

INSERT into travel_plan(id, account_id, is_favorited, is_published)
VALUES (1, 1, false, false);

INSERT into moderator_account(id, email, first_name, last_name, password, password_salt, username)
VALUES (10, 'admin@gmail.com', 'Barry', 'Allen', 'password1', 'password1_salt', 'admin');

INSERT into post(id, created_at, posted_date, travel_plan) 
VALUES (1, '2019-01-01', '2019-01-01', 1);

INSERT into post_comment(id, commented_date, commented_on, content, made_by)
VALUES (1, '2019-01-01', 1, 'content', 3);

INSERT into follow(follower, following)
VALUES (1, 3)

INSERT into follow_request(follower, following)
VALUES (1, 2);

INSERT into travel_plan_location(id, city, country, end_date, start_date, travel_plan_id)
VALUES (1, 'Sydney', 'Australia', '2018-12-31', '2018-12-01', 1);

INSERT into travel_plan_collaborator(collaborator_id, travel_plan_id) 
VALUES (3, 1)

INSERT into banned_user(account_id) VALUES (4);

INSERT into post_like(post_id, user_id) VALUES (1, 3);

INSERT into comment_like(comment_id, user_id) VALUES (1, 3);