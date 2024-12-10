INSERT INTO achievement (title, description, rarity, points, created_at, updated_at)
VALUES
    ('COLLECTOR', 'For 100 goals', 3, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MR PRODUCTIVITY', 'For 1000 finished tasks', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EXPERT', 'For 1000 comments', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SENSEI', 'For 30 mentees', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MANAGER', 'For 10 teams', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('CELEBRITY', 'For 1 000 000 subscribers', 4, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('WRITER', 'For 100 posts published', 2, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HANDSOME', 'For uploaded profile photo', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO country (title)
VALUES
    ('United States'),
    ('United Kingdom'),
    ('Australia'),
    ('France');

INSERT INTO users (username, email, phone, password, active, about_me, country_id, city, experience, created_at, updated_at)
VALUES
    ('JohnDoe', 'johndoe@example.com', '1234567890', 'password1', true, 'About John Doe', 1, 'New York', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('JaneSmith', 'janesmith@example.com', '0987654321', 'password2', true, 'About Jane Smith', 2, 'London', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MichaelJohnson', 'michaeljohnson@example.com', '1112223333', 'password3', true, 'About Michael Johnson', 1, 'Sydney', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EmilyDavis', 'emilydavis@example.com', '4445556666', 'password4', true, 'About Emily Davis', 3, 'Paris', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('WilliamTaylor', 'williamtaylor@example.com', '7778889999', 'password5', true, 'About William Taylor', 2, 'Toronto', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('OliviaAnderson', 'oliviaanderson@example.com', '0001112222', 'password6', true, 'About Olivia Anderson', 1, 'Berlin', 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('JamesWilson', 'jameswilson@example.com', '3334445555', 'password7', true, 'About James Wilson', 3, 'Tokyo', 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SophiaMartin', 'sophiamartin@example.com', '6667778888', 'password8', true, 'About Sophia Martin', 4, 'Rome', 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('BenjaminThompson', 'benjaminthompson@example.com', '9990001111', 'password9', true, 'About Benjamin Thompson', 4, 'Moscow', 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('AvaHarris', 'avaharris@example.com', '2223334444', 'password10', true, 'About Ava Harris', 3, 'Madrid', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
