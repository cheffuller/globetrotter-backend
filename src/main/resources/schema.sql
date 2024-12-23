DROP DATABASE IF EXISTS revature;
CREATE DATABASE IF NOT EXISTS revature;
USE revature;

CREATE TABLE user_account (
	id INT AUTO_INCREMENT,
	address VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    password_salt VARCHAR(100) NOT NULL,
    username VARCHAR(40) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE user_profile (
	id INT AUTO_INCREMENT,
    account_id INT NOT NULL,
	bio VARCHAR(1000),
    display_name VARCHAR(50) NOT NULL,
    is_private BOOL NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES user_account(id)
);

CREATE TABLE travel_plan (
	id INT AUTO_INCREMENT,
    account_id int NOT NULL,
    is_favorited BOOL NOT NULL,
    is_published BOOL NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES user_account(id)
);

CREATE TABLE moderator_account (
	id INT AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    password_salt VARCHAR(100) NOT NULL,
    username VARCHAR(40) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE post (
	id INT AUTO_INCREMENT,
    created_at DATE NOT NULL,
    posted_date DATE,
    travel_plan INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (travel_plan) REFERENCES travel_plan(id)
);

CREATE TABLE post_comment (
	id INT AUTO_INCREMENT,
    commented_date DATE NOT NULL,
    commented_on int NOT NULL,
    content VARCHAR(1000) NOT NULL,
    made_by INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (commented_on) REFERENCES post(id),
    FOREIGN KEY (made_by) REFERENCES user_account(id)
);

CREATE TABLE follow (
	follower INT NOT NULL,
    following INT NOT NULL,
    FOREIGN KEY (follower) REFERENCES user_account(id),
    FOREIGN KEY (following) REFERENCES user_account(id)
);

CREATE TABLE follow_request (
	follower INT NOT NULL,
    following INT NOT NULL,
    FOREIGN KEY (follower) REFERENCES user_account(id),
    FOREIGN KEY (following) REFERENCES user_account(id)
);

CREATE TABLE travel_plan_location (
	id INT AUTO_INCREMENT,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    end_date DATE NOT NULL,
    start_date DATE NOT NULL,
    travel_plan_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(travel_plan_id) REFERENCES travel_plan(id)
);

CREATE TABLE travel_plan_collaborator (
	collaborator_id INT NOT NULL,
	travel_plan_id INT NOT NULL,
	FOREIGN KEY (collaborator_id) REFERENCES user_account(id),
    FOREIGN KEY (travel_plan_id) REFERENCES travel_plan(id)
);

CREATE TABLE banned_user (
	account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES user_account(id)
);

CREATE TABLE post_like (
    post_id	INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (user_id) REFERENCES user_account(id)
);
    
CREATE TABLE comment_like (
    comment_id	INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (comment_id) REFERENCES post_comment(id),
    FOREIGN KEY (user_id) REFERENCES user_account(id)
);