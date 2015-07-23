'use strict';

var samlibServices = angular.module('samlibServices', []);

samlibServices.factory('AuthorService', function($http){
    return {
        getAuthorDetails: function($scope, authorId){
            console.log("AuthorService.getAuthorDetails("+authorId+")");
            $http.get("/author/"+authorId)
                .success(function (data, status) {
                    $scope.authorDetails = data;
                    $scope.pageTitle = $scope.authorDetails.name;
                    $scope.status = status;
                })
                .error(function (data, status) {
                    $scope.authorDetails = data || "Request failed";
                    $scope.status = status;
                });
        }
    };
});

samlibServices.factory('UtilsService', function($http) {
    return {
        checkUpdates: function ($scope) {
            $http.get("/utils/check").
                success(function (data, status) {
                    $scope.updateStatistic = data;
                    $scope.status = status;
                }).
                error(function (data, status) {
                    $scope.updateStatistic = data || "Request failed";
                    $scope.status = status;
                })
        },

        getStatistic: function ($scope) {
            $http.get("/utils/statistic").
                success(function (data, status) {
                    $scope.statistic = data;
                    $scope.status = status;
                }).
                error(function (data, status) {
                    $scope.statistic = data || "Request failed";
                    $scope.status = status;
                });
        }
    }
});

samlibServices.factory('SubscriptionService', function($http){
    return {
        getAuthorList: function ($scope) {
            console.log("AuthorService.getAuthorList");
            $http.get("/subscription/list")
                .success(function (data, status) {
                    $scope.subscriptions = data;
                    $scope.pageTitle='Samlib';
                    $scope.status = status;
                })
                .error(function (data, status) {
                    $scope.subscriptions = data || "Request failed";
                    $scope.status = status;
                    console.error("subscriptions ERROR:", $scope.subscriptions);
                });
        },

        addAuthor: function($scope, authorUrl){
            $http.post("/subscription/subscribe/url", authorUrl).success(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            })
                .error(function (data, status) {
                    $scope.statistic = data;
                    $scope.status = status;
                });
        },

        cleanUnread: function($scope, subscription){
            var link = "/subscription/" + subscription.subscriptionId + "/unread/all";
            var s_index = $scope.subscriptions.indexOf(subscription);
            console.log("s_index", s_index);
            $http.delete(link)
                .success(function (data, status) {
                    $scope.subscriptions[s_index] = data;
                    $scope.status = status;
                })
                .error(function (data, status) {
                    $scope.subscriptions[s_index] = data;
                    $scope.status = status;
                });
        },

        addWritingToUnreadList: function($scope, subscriptionId, writingId){
        var link = "/subscription/"+subscriptionId+"/unread/"+writingId;
        $http.post(link)
            .success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
            })
            .error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
        },


        removeWritingFromUnreadList: function ($scope, subscriptionId, writingId){
        var link = "/subscription/"+subscriptionId+"/unread/"+writingId;
        $http.delete(link)
            .success(function (data, status) {
                $scope.authorDetails = data;
                $scope.status = status;
            })
            .error(function (data, status) {
                $scope.authorDetails = data || "Request failed";
                $scope.status = status;
            });
        }

    };
});

samlibServices.factory('AdminService', function($http){
    return {
        doImport: function ($scope, authors){
            $http.post("/utils/import", authors)
                .success(function(data, status){
                    $scope.importResult = data;
                    $scope.status = status;
                })
                .error(function(data, status){
                    $scope.importResult = data || "Request failed";
                    $scope.status = status;
                });
        },
        doExport: function($scope){
            $http.get('/utils/export')
                .success(function(data, status){
                    $scope.exportResult = data;
                    $scope.status = status;
                })
                .error(function(data, status){
                    $scope.exportResult = data || "Request failed";
                    $scope.status = status;
                });

        },
        doBackup: function($scope){

        },

        doRestore: function($scope, data){

        },

        getAuthorList: function($scope){
            $http.get('/author/list')
                .success(function(data, status){
                    $scope.allAuthors = data;
                    $scope.status = status;
                })
                .error(function(data, status){
                    $scope.allAuthors = data || "Request failed";
                    $scope.status = status;
                });
        },

        removeAuthor: function($scope, authorId){
            $http.delete("/author/"+authorId)
                .success(function(data, status){
                    $scope.removeResult = data;
                    $scope.status = status;
                })
                .error(function(data, status){
                    $scope.removeResult = data || "Request failed";
                    $scope.status = status;
                });
            getAuthorList($scope);
        }
    }
});