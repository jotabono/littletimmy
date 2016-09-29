(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EmpresaDeleteController',EmpresaDeleteController);

    EmpresaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Empresa'];

    function EmpresaDeleteController($uibModalInstance, entity, Empresa) {
        var vm = this;

        vm.empresa = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Empresa.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
