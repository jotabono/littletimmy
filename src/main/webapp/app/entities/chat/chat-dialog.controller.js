(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDialogController', ChatDialogController);

    ChatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Chat', 'User'];

    function ChatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Chat, User) {
        var vm = this;

        vm.chat = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chat.id !== null) {
                Chat.update(vm.chat, onSaveSuccess, onSaveError);
            } else {
                Chat.save(vm.chat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:chatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
