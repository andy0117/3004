-- Schema for dbPOLL
-- Louis Stowasser 25/5/11
-- CSSE3004 Group F

CREATE TABLE Questions(
	questID NUMBER(6) PRIMARY KEY,
	demographic CHAR(1),
	responseType CHAR(1),
	question VARCHAR2(255) NOT NULL
);

CREATE TABLE Comparitives(
	questID NUMBER(6) PRIMARY KEY,
	compareTo NUMBER(6)
);

CREATE TABLE Rankings(
	questID NUMBER(6) PRIMARY KEY,
	responseID NUMBER(6) PRIMARY KEY,
	weight NUMBER(6)
);

CREATE TABLE Responses(
	reponseID NUMBER(6) PRIMARY KEY,
	keypad CHAR(1),
	response VARCHAR2(255) NOT NULL,
	questID NUMBER(6) NOT NULL
);

CREATE TABLE Answers(
	answerID NUMBER(6) PRIMARY KEY,
	response VARCHAR2(255), -- One of these is going to be NULL
	keypad CHAR(1),	        -- Means a lot less tables though :)
	questID NUMBER(6) NOT NULL
);

CREATE TABLE Polls(
	pollID NUMBER(6) PRIMARY KEY,
	pollName VARCHAR2(255) NOT NULL,
	location VARCHAR2(255) NOT NULL,
);

CREATE TABLE Users(
	userID NUMBER(6) PRIMARY KEY,
	userName VARCHAR2(255) NOT NULL,
	password VARCHAR2(255) NOT NULL,
	email VARCHAR2(255) NOT NULL,
	location VARCHAR2(255) NOT NULL
);

CREATE TABLE Assigned(
	userID NUMBER(6) PRIMARY KEY,
	pollID NUMBER(6) PRIMARY KEY,
	role VARCHAR(255),
	CONSTRAINT userRole CHECK (role IN ('Web User', 'Key User', 'Poll Master', 'Poll Creator', 'Poll Admin', 'System Admin'))
);