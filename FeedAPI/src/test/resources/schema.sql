-- Minimal pre-created schema for the test profile. FeedAPI's ddl-auto is
-- "validate" (it owns none of these tables - PostAPI/UserAPI do in the real
-- deployment), so the H2 test database needs these 4 tables to already exist
-- with the exact columns FeedAPI's read-only entities map onto, or context
-- startup fails validation.

create table posts (
    id uuid not null primary key,
    author_id uuid not null,
    title varchar(256) not null,
    description varchar(2000),
    cooking_time varchar(64),
    difficulty varchar(32),
    serving_size int,
    is_published boolean not null,
    like_count int not null,
    comment_count int not null,
    save_count int,
    created_at timestamp not null,
    updated_at timestamp
);

create table follows (
    id uuid not null primary key,
    follower_id uuid not null,
    followee_id uuid not null,
    created_at timestamp
);

create table post_tags (
    id uuid not null primary key,
    post_id uuid not null,
    tag varchar(64) not null
);

create table post_media (
    id uuid not null primary key,
    post_id uuid not null,
    url varchar(2000) not null,
    media_type varchar(16),
    sort_order int not null
);
