(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Trabajo', Trabajo);

    Trabajo.$inject = ['$resource', 'DateUtils'];

    function Trabajo ($resource, DateUtils) {
        var resourceUrl =  'api/trabajos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaInicio = DateUtils.convertDateTimeFromServer(data.fechaInicio);
                        data.fechaFin = DateUtils.convertDateTimeFromServer(data.fechaFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
