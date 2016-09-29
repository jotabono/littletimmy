(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Empresa', Empresa);

    Empresa.$inject = ['$resource', 'DateUtils'];

    function Empresa ($resource, DateUtils) {
        var resourceUrl =  'api/empresas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaFundacion = DateUtils.convertDateTimeFromServer(data.fechaFundacion);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
