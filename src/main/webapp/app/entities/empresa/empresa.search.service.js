(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('EmpresaSearch', EmpresaSearch);

    EmpresaSearch.$inject = ['$resource'];

    function EmpresaSearch($resource) {
        var resourceUrl =  'api/_search/empresas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
