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
                    data = angular.fromJson(data);
                    data.fechaInicio = DateUtils.convertLocalDateFromServer(data.fechaInicio);
                    data.fechaFinal = DateUtils.convertLocalDateFromServer(data.fechaFinal);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fechaInicio = DateUtils.convertLocalDateToServer(data.fechaInicio);
                    data.fechaFinal = DateUtils.convertLocalDateToServer(data.fechaFinal);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fechaInicio = DateUtils.convertLocalDateToServer(data.fechaInicio);
                    data.fechaFinal = DateUtils.convertLocalDateToServer(data.fechaFinal);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
