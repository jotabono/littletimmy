(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Messages', Messages);

    Messages.$inject = ['$resource', 'DateUtils'];

    function Messages ($resource, DateUtils) {
        var resourceUrl =  'api/messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sendDate = DateUtils.convertDateTimeFromServer(data.sendDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
