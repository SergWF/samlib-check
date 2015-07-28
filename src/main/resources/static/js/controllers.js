'use strict';

var samlibControllers = angular.module('samlibControllers', ['ngRoute']);

samlibControllers.controller("CommonCtrl", function($scope){
    $scope.changeTitle = function(pageTitle){
        $scope.pageTitle = pageTitle;
    }
});

samlibControllers.controller("UtilsCtrl", function($scope, SubscriptionService, UtilsService){
    $scope.updateStatistic={};

    $scope.addAuthor = function(){
        var authorUrl = $scope.newAuthorUrl;
        SubscriptionService.addAuthor($scope, authorUrl);
    };

    $scope.checkUpdates = function(){
        UtilsService.checkUpdates($scope);
    };
    $scope.getStatistic = function(){
        UtilsService.getStatistic($scope);
    };
});

samlibControllers.controller("AuthorListCtrl", function($scope, SubscriptionService, UtilsService){
    $scope.subscriptions = [];
    $scope.getTotal = function () {
        return $scope.subscriptions.length;
    };

    function refreshStatistic() {
        UtilsService.getStatistic($scope);
    }

    $scope.getAuthors = function () {
        SubscriptionService.getAuthorList($scope);
        refreshStatistic();
    };

    $scope.cleanUnread = function(subscription){
        SubscriptionService.cleanUnread($scope, subscription);
        refreshStatistic();
    }

    $scope.getAuthors();
});



samlibControllers.controller("AuthorDetailsCtrl", function($scope, $routeParams, AuthorService, SubscriptionService){
    $scope.getAuthorDetails = function(authorId){
        AuthorService.getAuthorDetails($scope, authorId);
    };

    $scope.changeUnreadState = function(writing){
        var subscriptionId =  $scope.authorDetails.subscriptionId;
        var writingId = writing.writingId;

        if(writing.unread){
            SubscriptionService.addWritingToUnreadList($scope, subscriptionId, writingId);
        }else{
            SubscriptionService.removeWritingFromUnreadList($scope, subscriptionId, writingId);
        }
    }

    $scope.getAuthorDetails($routeParams.authorId);

});


samlibControllers.controller("AdminCtrl", function($scope, $routeParams, AdminService){
    $scope.doImport = function(authorsFiles){
        var file = authorsFiles[0];
        console.log('file:', file);
        AdminService.doImport($scope, file);
    };

    $scope.doExport = function(){
        AdminService.doExport($scope);
    };

    $scope.doBackup = function(){
        AdminService.doBackup($scope);
    };

    $scope.doRestore = function(backupFiles){
        var file = backupFiles[0];
        console.log('file:', file);
        AdminService.doRestore($scope, file);
    };

    $scope.getAllAuthors = function(){
        AdminService.getAuthorList($scope);
    };
    $scope.removeAuthor = function(author){
        if(confirm("Do you really want to delete " + author.name + "?")) {
            console.log("delete", author);
            AdminService.removeAuthor($scope, author.id);
        }
    };

    $scope.getAllAuthors();

});