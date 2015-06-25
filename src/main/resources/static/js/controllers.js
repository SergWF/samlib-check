'use strict';

var samlibControllers = angular.module('samlibControllers', ['ngRoute']);


samlibControllers.controller("AuthorListCtrl", function($scope, $http){
    $scope.authors = [];
    $scope.getTotal = function () {
        return $scope.authors.length;
    };

    $scope.getAuthors = function () {
        $http.get("/author/list").
            success(function (data, status) {
                $scope.authors = data._embedded.authorList;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.authors = data || "Request failed";
                $scope.status = status;
            });

    };

    $scope.updateStatistic={};
    $scope.checkUpdates = function(){
        $http.put("/author/check").
            success(function (data, status) {
                $scope.updateStatistic = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.updateStatistic = data || "Request failed";
                $scope.status = status;
            });
    };
    $scope.getStatistic = function(){
        $http.get("/author/statistic").
            success(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.statistic = data || "Request failed";
                $scope.status = status;
            });
    };
    $scope.addAuthor = function(){
        console.log("add:", $scope.newAuthorUrl);
        var newAuthor = {
            url: $scope.newAuthorUrl
        }
        console.log("add:", newAuthor);
        $http.post("/author", newAuthor, {'Content-Type': 'application/json'}).success(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            })
            .error(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            });
    };

    $scope.getAuthors();
});

samlibControllers.controller("AuthorDetailsCtrl", function($scope, $http, $routeParams){
    $scope.getAuthorDetails = function(authorId){
        $http.get("/author/"+authorId).
            success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
                console.log($scope.authorDetails);
            }).
            error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
    };
    $scope.getAuthorDetails($routeParams.authorId);
});