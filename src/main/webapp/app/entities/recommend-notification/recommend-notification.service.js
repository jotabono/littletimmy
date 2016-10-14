(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Recommend_notification', Recommend_notification);

    Recommend_notification.$inject = ['$resource', 'DateUtils'];

    function Recommend_notification ($resource, DateUtils) {
        var resourceUrl =  'api/recommend-notifications/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
