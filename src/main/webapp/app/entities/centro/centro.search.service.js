(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('CentroSearch', CentroSearch);

    CentroSearch.$inject = ['$resource'];

    function CentroSearch($resource) {
        var resourceUrl =  'api/_search/centros/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
