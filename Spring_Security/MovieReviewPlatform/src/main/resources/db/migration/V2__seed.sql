-- Demo users with fixed UUIDs and dummy hashed passwords (placeholders)
INSERT INTO users (id, username, password, display_name)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'alice', '{bcrypt}$2a$10$abcdefghijklmnopqrstuv', 'Alice'),
  ('22222222-2222-2222-2222-222222222222', 'bob',   '{bcrypt}$2a$10$abcdefghijklmnopqrstuv', 'Bob')
ON CONFLICT (id) DO NOTHING;

-- Demo movies with fixed UUIDs
INSERT INTO movies (id, title, year, genres, description)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Inception', 2010, ARRAY['Action','Sci-Fi']::text[], 'A thief who steals corporate secrets through dream-sharing technology.'),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'The Matrix', 1999, ARRAY['Action','Sci-Fi']::text[], 'A hacker discovers the shocking truth about his reality.'),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Interstellar', 2014, ARRAY['Adventure','Drama','Sci-Fi']::text[], 'A team travels through a wormhole in search of a new home for humanity.')
ON CONFLICT (id) DO NOTHING;

-- Demo reviews
INSERT INTO reviews (id, movie_id, user_id, rating, text)
VALUES
  ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 9, 'Mind-bending and visually stunning.'),
  ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 10, 'A genre-defining classic.')
ON CONFLICT (id) DO NOTHING;
