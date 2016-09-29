(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Recomendacion', Recomendacion);

    Recomendacion.$inject = ['$resource', 'DateUtils'];

    function Recomendacion ($resource, DateUtils) {
        var resourceUrl =  'api/recomendacions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaEnvio = DateUtils.convertDateTimeFromServer(data.fechaEnvio);
                        data.fechaResolucion = DateUtils.convertDateTimeFromServer(data.fechaResolucion);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
