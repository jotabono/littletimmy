(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('MessagesSearch', MessagesSearch);

    MessagesSearch.$inject = ['$resource'];

    function MessagesSearch($resource) {
        var resourceUrl =  'api/_search/messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
