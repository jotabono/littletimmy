(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('TrabajoSearch', TrabajoSearch);

    TrabajoSearch.$inject = ['$resource'];

    function TrabajoSearch($resource) {
        var resourceUrl =  'api/_search/trabajos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
