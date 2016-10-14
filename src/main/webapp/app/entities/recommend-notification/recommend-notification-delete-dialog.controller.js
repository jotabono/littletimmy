(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Recommend_notificationDeleteController',Recommend_notificationDeleteController);

    Recommend_notificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Recommend_notification'];

    function Recommend_notificationDeleteController($uibModalInstance, entity, Recommend_notification) {
        var vm = this;

        vm.recommend_notification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Recommend_notification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
