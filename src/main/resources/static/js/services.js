'use strict';

var samlibServices = angular.module('samlibServices', []);

samlibServices.factory('AuthorService', function($http){
    return {
        getAuthorDetails: function($scope, authorId){
            console.log("AuthorService.getAuthorDetails("+authorId+")");
            $http.get("/authors/"+authorId)
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
            $http.get("/authors")
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
            $http.post("/authors/subscribe/url", authorUrl).success(function (data, status) {
                $scope.statistic = data;
                $scope.status = status;
            })
                .error(function (data, status) {
                    $scope.statistic = data;
                    $scope.status = status;
                });
        },

        cleanUnread: function($scope, subscription){
            var link = "/authors/" + subscription.subscriptionId + "/unread/all";
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
        var link = "/authors/"+subscriptionId+"/unread/"+writingId;
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
        var link = "/authors/"+subscriptionId+"/unread/"+writingId;
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
        doImport: function ($scope, authorsFile){
            var formData = new FormData();
            formData.append("file", authorsFile);
            $http.post("/admin/import"
                , formData
                ,{
                    headers: {'Content-Type': undefined}
                    , transformRequest: angular.identity
                })
                .success(function(data, status){
                    $scope.externalData = JSON.stringify(data);
                    console.log('json', $scope.externalData);
                    $scope.status = status;
                })
                .error(function(data, status){
                    $scope.externalData = data || "Request failed";
                    $scope.status = status;
                });
        },
        doExport: function($scope){
            $http.get('/admin/export')
                .succsess(function(data, status){
                    console.log("export OK", data);
                })
                .error(function(data, status){
                    console.error("export failed", status, data)
                });
        },
        doBackup: function($scope){
            $http.get('/admin/backup')
                .succsess(function(data, status){
                    console.log("backup OK", data);
                })
                .error(function(data, status){
                    console.error("backup failed", status, data)
                });
        },

        doRestore: function($scope, backupFile){
            var formData = new FormData();
            formData.append("file", backupFile);
            $http.post("/admin/restore"
                , formData
                ,{
                    headers: {'Content-Type': undefined}
                    , transformRequest: angular.identity
                })
                .success(function(data, status){
                    console.log("restored", data);
                })
                .error(function(data, status){
                    console.error("not restored", status, data)
                });
        },

        getAuthorList: function($scope){
            $http.get('/authors/list')
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
            $http.delete("/authors/"+authorId)
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