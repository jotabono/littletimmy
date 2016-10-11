(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('Friend_userSearch', Friend_userSearch);

    Friend_userSearch.$inject = ['$resource'];

    function Friend_userSearch($resource) {
        var resourceUrl =  'api/_search/friend-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
