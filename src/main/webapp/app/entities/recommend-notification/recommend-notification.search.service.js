(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('Recommend_notificationSearch', Recommend_notificationSearch);

    Recommend_notificationSearch.$inject = ['$resource'];

    function Recommend_notificationSearch($resource) {
        var resourceUrl =  'api/_search/recommend-notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
