(function() {
    'use strict';
    angular
        .module('littletimmyApp')
        .factory('Chat', Chat);

    Chat.$inject = ['$resource', 'DateUtils'];

    function Chat ($resource, DateUtils) {
        var resourceUrl =  'api/chats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
