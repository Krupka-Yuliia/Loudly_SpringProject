create table artist
(
    id        int auto_increment primary key,
    biography varchar(255) null,
    nickname  varchar(255) null
);

create table song
(
    id        int auto_increment  primary key,
    artist_id int            not null,
    file      varbinary(255) null,
    format    varchar(255)   null,
    genre     varchar(255)   null,
    title     varchar(255)   null,
    year      int   null,
    constraint fkArtist
        foreign key (artist_id) references artist (id)
);

create table user
(
    id       int auto_increment
        primary key,
    email    varchar(255) null,
    password varchar(255) null,
    username varchar(255) null
);

create table playlist
(
    id      int auto_increment
        primary key,
    name    varchar(255) null,
    user_id int          not null,
    foreign key (user_id) references user (id)
);


create table playlist_songs
(
    playlist_id int not null,
    song_id     int not null,
    constraint fkSong
        foreign key (song_id) references song (id),
    constraint fkPlaylist
        foreign key (playlist_id) references playlist (id)
);
















