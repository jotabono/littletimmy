(function () {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'},
            'getFriendsUser': {
                method: 'GET',
                isArray: true,
                url: 'api/user/:login/friends'
            },
            'getTrabajosUser': {
                method: 'GET',
                isArray: true,
                url: 'api/trabajos/usuario/:login'
            },
            'getEstudiosUser': {
                method: 'GET',
                isArray: true,
                url: 'api/estudios/user/:login'
            }
        });
        return service;
    }
})();
