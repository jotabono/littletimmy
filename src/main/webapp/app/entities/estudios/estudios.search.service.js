(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('EstudiosSearch', EstudiosSearch);

    EstudiosSearch.$inject = ['$resource'];

    function EstudiosSearch($resource) {
        var resourceUrl =  'api/_search/estudios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
