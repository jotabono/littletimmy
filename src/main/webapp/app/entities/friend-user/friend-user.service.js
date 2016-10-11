(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Friend_user', Friend_user);

    Friend_user.$inject = ['$resource', 'DateUtils'];

    function Friend_user ($resource, DateUtils) {
        var resourceUrl =  'api/friend-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.friendship_date = DateUtils.convertDateTimeFromServer(data.friendship_date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
