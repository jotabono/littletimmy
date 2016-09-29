(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Estudios', Estudios);

    Estudios.$inject = ['$resource', 'DateUtils'];

    function Estudios ($resource, DateUtils) {
        var resourceUrl =  'api/estudios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaInicio = DateUtils.convertDateTimeFromServer(data.fechaInicio);
                        data.fechaFinal = DateUtils.convertDateTimeFromServer(data.fechaFinal);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
