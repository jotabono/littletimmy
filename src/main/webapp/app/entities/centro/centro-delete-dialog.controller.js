(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('CentroDeleteController',CentroDeleteController);

    CentroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Centro'];

    function CentroDeleteController($uibModalInstance, entity, Centro) {
        var vm = this;

        vm.centro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Centro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
