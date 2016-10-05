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
                        data.fechaInicio = DateUtils.convertLocalDateFromServer(data.fechaInicio);
                        data.fechaFinal = DateUtils.convertLocalDateFromServer(data.fechaFinal);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaInicio = DateUtils.convertLocalDateToServer(copy.fechaInicio);
                    copy.fechaFinal = DateUtils.convertLocalDateToServer(copy.fechaFinal);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaInicio = DateUtils.convertLocalDateToServer(copy.fechaInicio);
                    copy.fechaFinal = DateUtils.convertLocalDateToServer(copy.fechaFinal);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
