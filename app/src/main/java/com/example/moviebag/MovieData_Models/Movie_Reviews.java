package com.example.moviebag.MovieData_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie_Reviews {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Movie_Reviews(Integer id, Integer page, List<Result> results, Integer totalPages, Integer totalResults) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    //    Subclass here
    public class Result {

        @SerializedName("author")
        @Expose
        private String author;
        @SerializedName("author_details")
        @Expose
        private AuthorDetails authorDetails;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("url")
        @Expose
        private String url;

        public Result(String author, AuthorDetails authorDetails, String content, String createdAt, String id, String updatedAt, String url) {
            this.author = author;
            this.authorDetails = authorDetails;
            this.content = content;
            this.createdAt = createdAt;
            this.id = id;
            this.updatedAt = updatedAt;
            this.url = url;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public AuthorDetails getAuthorDetails() {
            return authorDetails;
        }

        public void setAuthorDetails(AuthorDetails authorDetails) {
            this.authorDetails = authorDetails;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class AuthorDetails {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("avatar_path")
        @Expose
        private String avatarPath;
        @SerializedName("rating")
        @Expose
        private Integer rating;

        public AuthorDetails(String name, String username, String avatarPath, Integer rating) {
            this.name = name;
            this.username = username;
            this.avatarPath = avatarPath;
            this.rating = rating;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatarPath() {
            return avatarPath;
        }

        public void setAvatarPath(String avatarPath) {
            this.avatarPath = avatarPath;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

    }
}
