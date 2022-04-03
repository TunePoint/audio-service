-- noinspection SqlNoDataSourceInspectionForFile

CREATE SCHEMA if NOT EXISTS audio;

CREATE TABLE audio.genres
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE audio.audio
(
    id SERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content_id VARCHAR(64) NOT NULL,
    cover_id VARCHAR(64),
    author_name VARCHAR(255),
    description VARCHAR(255),
    uploaded_time TIMESTAMP,
    duration_sec INTEGER,
    is_private BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE audio.audio_genres
(
    audio_id INTEGER REFERENCES audio.audio(id),
    genre_id INTEGER REFERENCES audio.genres(id),

    CONSTRAINT audio_genres_pk PRIMARY KEY(audio_id, genre_id)
);

CREATE TABLE audio.audio_stats (
    id INTEGER PRIMARY KEY REFERENCES audio.audio(id),

    like_count INTEGER DEFAULT 0,
    listening_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE audio.audio_likes
(
    audio_id INTEGER REFERENCES audio.audio(id),
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP,

    CONSTRAINT audio_likes_pk PRIMARY KEY(audio_id, user_id)
);

CREATE TABLE audio.audio_listening
(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    audio_id INTEGER REFERENCES audio.audio(id),
    listening_time TIMESTAMP
);

CREATE TABLE audio.comments
(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    audio_id INTEGER REFERENCES audio.audio(id),
    content VARCHAR(512),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    audio_timestamp INTEGER,
    is_deleted BOOLEAN DEFAULT false,
    is_edited BOOLEAN DEFAULT false,
    reply_to INTEGER REFERENCES audio.comments(id)
);

CREATE TABLE audio.comments_stats
(
    id INTEGER PRIMARY KEY REFERENCES audio.comments(id),
    like_count INTEGER DEFAULT 0 NOT NULL,
    reply_count INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE audio.comments_likes
(
    comment_id INTEGER REFERENCES audio.comments(id),
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP,

    CONSTRAINT comments_likes_pk PRIMARY KEY (comment_id, user_id)
);


CREATE FUNCTION create_audio_stats() RETURNS trigger AS $$
BEGIN
    INSERT INTO audio.audio_stats(id) values (new.id);
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER audio_created AFTER INSERT ON audio.audio
    FOR EACH ROW EXECUTE procedure create_audio_stats();

CREATE FUNCTION create_comment_stats() RETURNS trigger AS $$
BEGIN
    INSERT INTO audio.comments_stats(id) values (new.id);
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_created AFTER INSERT ON audio.comments
    FOR EACH ROW EXECUTE procedure create_comment_stats();

CREATE FUNCTION update_comment_stats_like() RETURNS trigger AS $$
BEGIN
    UPDATE audio.comments_stats SET like_count = like_count + 1 WHERE id = new.comment_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_liked AFTER INSERT ON audio.comments_likes
    FOR EACH ROW EXECUTE procedure update_comment_stats_like();

CREATE FUNCTION update_comment_stats_unlike() RETURNS trigger AS $$
BEGIN
    UPDATE audio.comments_stats SET like_count = like_count - 1 WHERE id = old.comment_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_unliked AFTER DELETE ON audio.comments_likes
    FOR EACH ROW EXECUTE procedure update_comment_stats_unlike();

CREATE FUNCTION update_comment_stats_reply() RETURNS trigger AS $$
BEGIN
    IF new.reply_to IS NOT NULL THEN
        UPDATE audio.comments_stats SET reply_count = reply_count + 1 WHERE id = new.reply_to;
    END IF;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_reply AFTER INSERT ON audio.comments
    FOR EACH ROW EXECUTE procedure update_comment_stats_reply();

CREATE FUNCTION update_audio_stats_comment_insert() RETURNS trigger AS $$
BEGIN
    UPDATE audio.audio_stats SET comment_count = comment_count + 1 WHERE id = new.audio_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_insert AFTER INSERT ON audio.comments
    FOR EACH ROW EXECUTE procedure update_audio_stats_comment_insert();

CREATE FUNCTION update_audio_stats_comment_delete() RETURNS trigger AS $$
BEGIN
    UPDATE audio.audio_stats SET comment_count = comment_count - 1 WHERE id = new.audio_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER comment_delete AFTER DELETE ON audio.comments
    FOR EACH ROW EXECUTE procedure update_audio_stats_comment_delete();

CREATE FUNCTION update_audio_stats_like() RETURNS trigger AS $$
BEGIN
    UPDATE audio.audio_stats SET like_count = like_count + 1 WHERE id = new.audio_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER audio_liked AFTER INSERT ON audio.audio_likes
    FOR EACH ROW EXECUTE procedure update_audio_stats_like();

CREATE FUNCTION update_audio_stats_unlike() RETURNS trigger AS $$
BEGIN
    UPDATE audio.audio_stats SET like_count = like_count - 1 WHERE id = old.audio_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER audio_unliked AFTER DELETE ON audio.audio_likes
    FOR EACH ROW EXECUTE procedure update_audio_stats_unlike();

CREATE FUNCTION update_audio_stats_listening() RETURNS trigger AS $$
BEGIN
    UPDATE audio.audio_stats SET listening_count = audio_stats.listening_count + 1 WHERE id = new.audio_id;
    RETURN new;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER audio_listened AFTER INSERT ON audio.audio_listening
    FOR EACH ROW EXECUTE procedure update_audio_stats_listening();


