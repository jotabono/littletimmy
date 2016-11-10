(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .factory('Account', Account);

    Account.$inject = ['$resource', 'DateUtils'];

    function Account ($resource, DateUtils) {
        var service = $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        if(response.data){
                            response.data = angular.fromJson(response.data);
                            response.data.fecha_nacimiento = DateUtils.convertLocalDateFromServer(response.data.fecha_nacimiento);
                        }
                        console.log(response);
                        return response;
                    }
                }
            }
        });

        return service;
    }
})();
