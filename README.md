# Loudly Spring Project

## Functional Requirements:
- **As a User**, I want to be able to:
  - Search for a song
  - Play a song
  - Create a playlist
  - Read a playlist
  - Update a playlist
  - Delete a playlist
  - Seek songs by genre
  - Seek songs by artist

- **As an Admin**, I want to be able to:
  - Add songs/artists
  - Delete songs/artists

## REST API Endpoints:

| Method | Path                                | Description                      |
|--------|-------------------------------------|----------------------------------|
| GET    | `/songs`                            | Get all songs                    |
| GET    | `/artists`                          | Get all artists                  |
| GET    | `/songs/search`                     | Search for songs                 |
| GET    | `/songs/{id}/play`                  | Play a song                      |
| GET    | `/users`                            | Get all users                    |
| GET    | `/songs/genre`                      | Get songs by genre               |
| GET    | `/songs/artist/{id}`                | Get songs by artist              |
| GET    | `/playlists/{id}`                   | Read a playlist                  |
| POST   | `/playlists`                        | Create a playlist                |
| PUT    | `/playlists/{id}/songs/{songId}`    | Add song to a playlist           |
| DELETE | `/playlists/{id}/songs/{songId}`    | Delete song from a playlist      |
| DELETE | `/playlists/{id}`                   | Delete a playlist                |
| POST   | `/songs`                            | Add a song (Admin)               |
| POST   | `/artists`                          | Add an artist (Admin)            |
| DELETE | `/songs/{id}`                       | Delete a song (Admin)            |
| DELETE | `/artists/{id}`                     | Delete an artist (Admin)         |
| POST   | `/auth`                             | Auth                             |
| POST   | `/logout`                           | Log out                          |


<img width="570" alt="Знімок екрана 2024-10-29 о 13 39 15" src="https://github.com/user-attachments/assets/2352d70d-0e01-4f3b-9515-153435009ae6">
<img width="676" alt="Знімок екрана 2024-10-29 о 13 41 03" src="https://github.com/user-attachments/assets/453792e8-dee7-4f2d-8159-348cf696df93">
<img width="695" alt="Знімок екрана 2024-10-29 о 13 40 56" src="https://github.com/user-attachments/assets/4146c32e-fbd0-4630-b565-458d5f13cf66">
<img width="694" alt="Знімок екрана 2024-10-29 о 13 40 52" src="https://github.com/user-attachments/assets/ff5d5943-f771-40db-b828-f178dfa47e36">
<img width="641" alt="Знімок екрана 2024-10-29 о 13 40 46" src="https://github.com/user-attachments/assets/ea26fb92-fcb6-47fa-b853-cf138059ad26">



