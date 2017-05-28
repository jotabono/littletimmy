(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('Friendship_notificationSearch', Friendship_notificationSearch);

    Friendship_notificationSearch.$inject = ['$resource'];

    function Friendship_notificationSearch($resource) {
        var resourceUrl =  'api/_search/friendship-notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
