DROP TABLE Questions;

DROP TABLE Comparitives;

DROP TABLE Responses;

DROP TABLE Ranking;

DROP TABLE Answers;

DROP TABLE KeyAnswers;

DROP TABLE ShortAnswers;

DROP TABLE Polls;

DROP TABLE Users;

ALTER TABLE Assigned DROP CONSTRAINT userRole;
ALTER TABLE Assigned DROP CONSTRAINT pk_Assigned;
DROP TABLE Assigned;