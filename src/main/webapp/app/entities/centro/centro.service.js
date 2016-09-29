(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Centro', Centro);

    Centro.$inject = ['$resource', 'DateUtils'];

    function Centro ($resource, DateUtils) {
        var resourceUrl =  'api/centros/:id';

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
