'use strict';

var samlibControllers = angular.module('samlibControllers', ['ngRoute']);

samlibControllers.controller("UtilsCtrl", function($scope, $http){
    $scope.updateStatistic={};

    $scope.addAuthor = function(){
        var authorUrl = $scope.newAuthorUrl;
        console.log("add:", authorUrl);

        $http.post("/subscription/subscribe/url", authorUrl).success(function (data, status) {
            $scope.statistic = data;
            $scope.status = status;
        })
            .error(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            });
    };

    $scope.checkUpdates = function(){
        $http.get("/utils/check").
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
        $http.get("/utils/statistic").
            success(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.statistic = data || "Request failed";
                $scope.status = status;
            });
    };
});

samlibControllers.controller("AuthorListCtrl", function($scope, $http){
    $scope.subscriptions = [];
    $scope.getTotal = function () {
        return $scope.subscriptions.length;
    };

    $scope.getAuthors = function () {
        console.log("get authors");
        $http.get("/subscription/list").
            success(function (data, status) {
                $scope.subscriptions = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.subscriptions = data || "Request failed";
                $scope.status = status;
                console.error("subscriptions ERROR:", $scope.subscriptions);
            });
    };

    $scope.cleanUnread = function(subscription){
        var link = "/subscription/" + subscription.subscriptionId + "/unread/all";
        var s_index = $scope.subscriptions.indexOf(subscription);
        console.log("s_index", s_index);
        $http.delete(link).success(function (data, status) {
            $scope.subscriptions[s_index] = data;
            $scope.status = status;
        })
            .error(function (data, status) {
                $scope.subscriptions[s_index] = data;
                $scope.status = status;
            });
    }

    $scope.getAuthors();
});



samlibControllers.controller("AuthorDetailsCtrl", function($scope, $http, $routeParams){

    function removeFromUnreadList(writing){
        var link = "/subscription/"+$scope.authorDetails.subscriptionId+"/unread/"+writing.writingId;
        $http.delete(link).
            success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
    }

    function addToUnreadList(writing){
        var link = "/subscription/"+$scope.authorDetails.subscriptionId+"/unread/"+writing.writingId;
        $http.post(link).
            success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
    }

    $scope.getAuthorDetails = function(authorId){
        $http.get("/author/"+authorId).
            success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
            }).
            error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
    };

    $scope.changeUnreadState = function(writing){
        if(writing.unread){
            addToUnreadList(writing);
        }else{
            removeFromUnreadList(writing);
        }
    }



    $scope.getAuthorDetails($routeParams.authorId);

});