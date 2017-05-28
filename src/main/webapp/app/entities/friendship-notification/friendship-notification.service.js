(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Friendship_notification', Friendship_notification);

    Friendship_notification.$inject = ['$resource', 'DateUtils'];

    function Friendship_notification ($resource, DateUtils) {
        var resourceUrl =  'api/friendship-notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaRecibida = DateUtils.convertDateTimeFromServer(data.fechaRecibida);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getFNotificationsNotReaded':{
                method: 'GET',
                isArray: true,
                url: 'api/friendship-notifications/user-conected'
            }
        });
    }
})();
