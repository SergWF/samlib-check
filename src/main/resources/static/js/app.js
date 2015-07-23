'use strict';

var samlibApp = angular.module('samlibApp', [
    'samlibControllers', 'samlibServices'
]);


samlibApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/subscriptions', {
                templateUrl: 'partial/authors_list.html',
                controller: 'AuthorListCtrl'
            }).
            when('/subscriptions/:authorId', {
                templateUrl: 'partial/author_details.html',
                controller: 'AuthorDetailsCtrl'
            }).
            otherwise({
                redirectTo: '/subscriptions'
            });
    }]);

