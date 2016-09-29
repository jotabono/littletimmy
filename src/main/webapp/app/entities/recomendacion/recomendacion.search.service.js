(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('RecomendacionSearch', RecomendacionSearch);

    RecomendacionSearch.$inject = ['$resource'];

    function RecomendacionSearch($resource) {
        var resourceUrl =  'api/_search/recomendacions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
