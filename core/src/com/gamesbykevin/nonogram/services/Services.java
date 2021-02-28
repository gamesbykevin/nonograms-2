package com.gamesbykevin.nonogram.services;

public interface Services {

    void share(String shareSubject, String shareBody, String shareTitle);

    void rate(String ratingActivity, String ratingUrl, String ratingFailure);
}
