(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friendship_notificationDialogController', Friendship_notificationDialogController);

    Friendship_notificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Friendship_notification', 'User', 'Friend_user'];

    function Friendship_notificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Friendship_notification, User, Friend_user) {
        var vm = this;

        vm.friendship_notification = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.friend_users = Friend_user.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friendship_notification.id !== null) {
                Friendship_notification.update(vm.friendship_notification, onSaveSuccess, onSaveError);
            } else {
                Friendship_notification.save(vm.friendship_notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:friendship_notificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaRecibida = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
