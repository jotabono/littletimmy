(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionDeleteController',RecomendacionDeleteController);

    RecomendacionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Recomendacion'];

    function RecomendacionDeleteController($uibModalInstance, entity, Recomendacion) {
        var vm = this;

        vm.recomendacion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Recomendacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
