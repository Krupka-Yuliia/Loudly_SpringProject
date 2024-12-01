package com.loudlyapp.utils;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(Long songId) {
        super("Song not found: " + songId);
    }
}
