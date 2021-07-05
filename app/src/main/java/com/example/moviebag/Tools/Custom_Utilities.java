package com.example.moviebag.Tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Custom_Utilities {

    public String getLanguageName(String languageCode) {

        Locale language = new Locale(languageCode);
        Locale intoLanguage = new Locale("en");
        String movie_finalLanguage = language.getDisplayLanguage(intoLanguage);

        if (language == null || languageCode == "") {
            return "Not Available";
        } else {
            return movie_finalLanguage;
        }

    }

    public String getFormattedDateAndTime(String DateAndTime) {
        String finalDateAndTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = simpleDateFormat.parse(DateAndTime);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            finalDateAndTime = dateFormat.format(date) + " | " + timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (DateAndTime == null || DateAndTime == "") {
            return "Not Available";
        } else {
            return finalDateAndTime;
        }
    }

    public String getFormattedDate(String releaseDate) {
        String finalDate = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(releaseDate);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            finalDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    public String getMovieReleaseStatus(boolean status) {
        if (status) {
            return "Released";
        } else if (!status) {
            return "Not Released";
        } else {
            return "Not Available";
        }
    }

}
