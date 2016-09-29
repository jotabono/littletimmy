(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('EstudiosDialogController', EstudiosDialogController);

    EstudiosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Estudios'];

    function EstudiosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Estudios) {
        var vm = this;

        vm.estudios = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.estudios.id !== null) {
                Estudios.update(vm.estudios, onSaveSuccess, onSaveError);
            } else {
                Estudios.save(vm.estudios, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:estudiosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaInicio = false;
        vm.datePickerOpenStatus.fechaFinal = false;

        vm.setArchivos = function ($file, estudios) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        estudios.archivos = base64Data;
                        estudios.archivosContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
